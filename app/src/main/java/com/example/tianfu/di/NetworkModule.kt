package com.example.tianfu.di

import com.example.tianfu.config.Config
import com.example.tianfu.data.AuthRepository
import com.example.tianfu.data.PropertyRepository
import com.example.tianfu.network.api.ApiService
import com.example.tianfu.network.model.ApiResponse
import com.example.tianfu.network.model.RefreshTokenRequest
import com.example.tianfu.network.model.RefreshTokenResponse
import com.example.tianfu.utils.AppEventBus
import com.example.tianfu.config.AppStoreKeys
import com.example.tianfu.utils.DataStoreManager
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import timber.log.Timber

/**
 * 网络依赖注入模块。
 *
 * 提供 [Json] 解析器、Ktor [HttpClient]、[ApiService] 的单例注入。
 *
 * 已内置：
 * - Token 自动注入（拦截器）
 * - 401 自动刷新 Token 机制
 * - 统一日志输出
 *
 * TODO: 根据你的后端 API 修改 Token 刷新逻辑和认证路径
 */
val NetworkModule = module {

    // 1. 全局 Json 配置
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        }
    }

    // 2. Ktor HttpClient 实例（含拦截器）
    single {
        val jsonParser: Json = get()

        HttpClient(Android) {
            engine {
                connectTimeout = Config.CONNECT_TIMEOUT.toInt()
                socketTimeout = Config.CONNECT_TIMEOUT.toInt()
            }

            install(ContentNegotiation) {
                json(jsonParser)
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.tag("KtorNetwork").d(message)
                    }
                }
                level = LogLevel.BODY
            }

            install(DefaultRequest) {
                url(Config.BASE_URL)
                contentType(ContentType.Application.Json)
            }

            install(HttpSend) {
                maxSendCount = 2
            }

        }.apply {
            // Token 拦截器：自动注入 + 401 刷新
            plugin(HttpSend).intercept { request ->

                val currentToken = DataStoreManager.get(AppStoreKeys.AccessToken)

                // 注入 Token 到后端要求的请求头（user-authentication）
                if (currentToken.isNotEmpty()) {
                    request.headers.remove("user-authentication")
                    request.headers.append("user-authentication", currentToken)
                }

                var originalCall = execute(request)

                // 401 自动刷新 Token
                if (originalCall.response.status == HttpStatusCode.Unauthorized && !request.url.encodedPath.contains("api/auth")) {
                    Timber.w("AccessToken 过期，触发自动刷新流程...")

                    val refreshToken = DataStoreManager.get(AppStoreKeys.RefreshToken)

                    if (refreshToken.isNotEmpty()) {
                        try {
                            val refreshRequest = HttpRequestBuilder().apply {
                                url("${Config.BASE_URL}api/auth/refresh")
                                method = HttpMethod.Post
                                contentType(ContentType.Application.Json)
                                setBody(RefreshTokenRequest(refreshToken))
                            }

                            val refreshCall = execute(refreshRequest)

                            if (refreshCall.response.status == HttpStatusCode.OK) {
                                val responseBody: ApiResponse<RefreshTokenResponse> = refreshCall.response.body()

                                if (responseBody.code == 200 && responseBody.data != null) {
                                    val newAccessToken = responseBody.data.accessToken
                                    DataStoreManager.put(AppStoreKeys.AccessToken, newAccessToken)
                                    Timber.d("Token 刷新成功！使用新 Token 重试原请求...")

                                    request.headers.remove("user-authentication")
                                    request.headers.append("user-authentication", newAccessToken)
                                    originalCall = execute(request)
                                } else {
                                    handleAuthFailed("刷新 Token 失败: ${responseBody.msg}")
                                }
                            } else {
                                handleAuthFailed("刷新 Token 接口异常，状态码: ${refreshCall.response.status}")
                            }
                        } catch (e: Exception) {
                            handleAuthFailed("刷新 Token 过程抛出异常: ${e.message}")
                        }
                    } else {
                        handleAuthFailed("本地无有效的 RefreshToken")
                    }
                }

                originalCall
            }
        }
    }

    // 3. ApiService 实例
    single {
        ApiService(client = get())
    }

    // 4. AuthRepository 实例
    single { AuthRepository(apiService = get()) }

    // 5. PropertyRepository 实例
    single { PropertyRepository(apiService = get()) }
}

/**
 * 处理授权彻底失效的善后工作
 */
private suspend fun handleAuthFailed(reason: String) {
    Timber.e("授权彻底失效 -> $reason")
    DataStoreManager.put(AppStoreKeys.AccessToken, "")
    DataStoreManager.put(AppStoreKeys.RefreshToken, "")
    DataStoreManager.put(AppStoreKeys.IsLoggedIn, false)
    AppEventBus.emitForceLogout("登录已过期，请重新登录")
}
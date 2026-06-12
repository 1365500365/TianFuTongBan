package com.example.tianfu.network.api

import com.example.tianfu.config.Config
import com.example.tianfu.network.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.*
import timber.log.Timber

/**
 * 应用网络服务类。
 *
 * 所有后端 API 的调用入口，由 Koin 自动注入 HttpClient 实例。
 *
 * 使用示例（在 ViewModel 中）：
 * ```kotlin
 * class MyViewModel(private val apiService: ApiService) : BaseViewModel<MyUiState>(MyUiState()) {
 *     fun loadData() {
 *         viewModelScope.launch {
 *             val response = apiService.getExampleList()
 *             if (response.code == 200) { ... }
 *         }
 *     }
 * }
 * ```
 *
 * @property client Ktor 的 HttpClient 实例，由 Koin 自动注入
 */
class ApiService(private val client: HttpClient) {

    // ── 认证相关（开放路由） ──

    /**
     * 用户登录
     * @param request 包含账号、密码及应用ID的请求体
     * @return 包含 Token 和用户信息的响应
     *
     * TODO: 修改请求路径和参数以匹配你的后端 API
     */
    suspend fun login(request: UserLoginRequest): ApiResponse<LoginResponse<User>> {
        return client.post("api/apps/${Config.APP_ID}/auth/user/login") {
            setBody(request)
        }.body()
    }

    // ── 业务接口示例（受保护路由） ──

    /**
     * 获取当前用户信息
     *
     * TODO: 修改路径以匹配你的后端 API
     */
    suspend fun getUserProfile(): ApiResponse<User> {
        return client.get("api/apps/${Config.APP_ID}/user/me").body()
    }

    /**
     * 获取示例列表数据（带分页）
     *
     * TODO: 替换为你的实际业务接口
     *
     * @param page 页码（null 表示不分页）
     * @param pageSize 每页数量
     */
    suspend fun getExampleList(page: Int? = null, pageSize: Int? = null): ApiResponse<PagedData<ExampleItem>> {
        return client.get("api/apps/${Config.APP_ID}/items") {
            parameter("page", page)
            parameter("pageSize", pageSize)
        }.body()
    }

    /**
     * 旧版登录接口迁移：Spring Boot @RequestParam("account"/"password")
     * 使用 application/x-www-form-urlencoded 表单提交
     */
    suspend fun legacyLogin(account: String, password: String): ApiResponse<LoginResponseDto> {
        return client.submitForm(
            url = "api/apps/${Config.APP_ID}/auth/user/login",
            formParameters = Parameters.build {
                append("account", account)
                append("password", password)
            },
        ).body()
    }

    /**
     * 旧版注册接口迁移：UserDto 作为 JSON 请求体
     */
    suspend fun legacyRegister(user: UserDto): ApiResponse<Any> {
        return client.post("api/apps/${Config.APP_ID}/auth/user/register") {
            setBody(user)
        }.body()
    }

    /**
     * 旧版房产信息查询接口迁移：Query 参数 ownerName/idNumber/city
     *
     * 为了便于兼容老后端，这里先以字符串读取响应体并打印日志，
     * 再使用手动 Json 解析为 ApiResponse<List<PropertyInfoDto>>。
     * 如果解析失败，则抛出包含原始响应内容的异常，方便前端直接看到后端返回了什么。
     */
    suspend fun queryPropertyInfo(
        ownerName: String,
        idNumber: String,
        city: String,
    ): ApiResponse<List<PropertyInfoDto>> {
        val response = client.get("api/apps/${Config.APP_ID}/property-info") {
            parameter("ownerName", ownerName)
            parameter("idNumber", idNumber)
            parameter("city", city)
        }

        val rawText = try {
            response.bodyAsText()
        } catch (e: Exception) {
            Timber.e(e, "读取房产查询响应文本失败")
            throw e
        }

        Timber.tag("PropertyDebug").d("property-info raw response: %s", rawText)

        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        return try {
            val root = json.parseToJsonElement(rawText).jsonObject

            val code = root["code"]?.jsonPrimitive?.contentOrNull?.toIntOrNull() ?: 0
            val msg = root["msg"]?.jsonPrimitive?.contentOrNull ?: ""
            val count = root["count"]?.jsonPrimitive?.contentOrNull?.toIntOrNull()

            val dataElement = root["data"]
            val properties: List<PropertyInfoDto> = if (dataElement is JsonArray) {
                dataElement.mapNotNull { item ->
                    runCatching {
                        val obj = item.jsonObject

                        PropertyInfoDto(
                            district = obj["district"]?.jsonPrimitive?.contentOrNull ?: "",
                            street = obj["street"]?.jsonPrimitive?.contentOrNull ?: "",
                            doorNo = obj["doorNo"]?.jsonPrimitive?.contentOrNull ?: "",
                            buildingNo = obj["buildingNo"]?.jsonPrimitive?.contentOrNull?.toIntOrNull(),
                            unitNo = obj["unitNo"]?.jsonPrimitive?.contentOrNull?.toIntOrNull(),
                            floor = obj["floor"]?.jsonPrimitive?.contentOrNull ?: "",
                            roomNo = obj["roomNo"]?.jsonPrimitive?.contentOrNull?.toIntOrNull(),
                            plannedUse = obj["plannedUse"]?.jsonPrimitive?.contentOrNull ?: "",
                            areaInCasing = obj["areaInCasing"]?.jsonPrimitive?.contentOrNull?.toDoubleOrNull(),
                            sharedArea = obj["sharedArea"]?.jsonPrimitive?.contentOrNull?.toDoubleOrNull(),
                        )
                    }.getOrElse {
                        Timber.e(it, "解析单条房产信息失败: %s", item.toString())
                        null
                    }
                }
            } else {
                emptyList()
            }

            ApiResponse(
                code = code,
                msg = msg,
                data = properties,
                count = count
            )
        } catch (e: Exception) {
            Timber.e(e, "解析房产查询响应失败，原始内容: %s", rawText)
            throw RuntimeException("房产查询接口返回非预期内容: $rawText", e)
        }
    }
}

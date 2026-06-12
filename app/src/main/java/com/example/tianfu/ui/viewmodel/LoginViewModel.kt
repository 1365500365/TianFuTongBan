package com.example.tianfu.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tianfu.config.AppStoreKeys
import com.example.tianfu.data.AuthRepository
import com.example.tianfu.network.AuthManager
import com.example.tianfu.utils.DataStoreManager
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val message: String? = null
)

class LoginViewModel(
    private val repository: AuthRepository,
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun login(account: String, password: String, agreed: Boolean) {
        viewModelScope.launch {
            if (account.isBlank() || password.isBlank()) {
                uiState = LoginUiState(message = "账号和密码不能为空")
                return@launch
            }
            if (!agreed) {
                uiState = LoginUiState(message = "请先勾选同意服务协议")
                return@launch
            }

            uiState = LoginUiState(isLoading = true)

            try {
                val result = repository.login(account, password)
                uiState = if (result.code == 1) {
                    val data = result.data
                    if (data != null) {
                        // 内存会话：供当前进程内页面间快速访问
                        AuthManager.token = data.token
                        AuthManager.user = data.user

                        // 持久化会话：供网络拦截器自动注入 Authorization 头，重启后恢复用户信息
                        DataStoreManager.put(AppStoreKeys.AccessToken, data.token)
                        DataStoreManager.put(AppStoreKeys.IsLoggedIn, true)
                        DataStoreManager.put(AppStoreKeys.UserInfo, data.user)
                    }
                    LoginUiState(
                        success = true,
                        message = result.msg.ifBlank { "登录成功" }
                    )
                } else {
                    LoginUiState(
                        message = result.msg.ifBlank { "登录失败" }
                    )
                }
            } catch (e: Exception) {
                uiState = LoginUiState(
                    message = "网络错误：" + (e.message ?: "未知错误")
                )
            }
        }
    }

    fun consumeMessage() {
        uiState = uiState.copy(message = null)
    }

    fun resetSuccess() {
        uiState = uiState.copy(success = false)
    }
}

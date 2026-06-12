package com.example.tianfu.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tianfu.data.AuthRepository
import com.example.tianfu.network.model.UserDto
import kotlinx.coroutines.launch

data class RegisterUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val message: String? = null
)

class RegisterViewModel(
    private val repository: AuthRepository,
) : ViewModel() {

    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun register(
        realName: String,
        idNumber: String,
        phone: String,
        password: String,
        confirmPassword: String,
        agreed: Boolean
    ) {
        viewModelScope.launch {
            if (realName.isBlank() || idNumber.isBlank() || phone.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                uiState = RegisterUiState(message = "注册信息不完整")
                return@launch
            }
            if (password != confirmPassword) {
                uiState = RegisterUiState(message = "两次输入的密码不一致")
                return@launch
            }
            if (!agreed) {
                uiState = RegisterUiState(message = "请先勾选同意服务协议")
                return@launch
            }

            uiState = RegisterUiState(isLoading = true)

            try {
                val user = UserDto(
                    realName = realName.trim(),
                    idType = "居民身份证",
                    idNumber = idNumber.trim(),
                    phone = phone.trim(),
                    password = password
                )

                val result = repository.register(user)
                uiState = if (result.code == 1) {
                    RegisterUiState(
                        success = true,
                        message = if (result.msg.isBlank()) "注册成功" else result.msg
                    )
                } else {
                    RegisterUiState(
                        message = if (result.msg.isBlank()) "注册失败" else result.msg
                    )
                }
            } catch (e: Exception) {
                uiState = RegisterUiState(
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

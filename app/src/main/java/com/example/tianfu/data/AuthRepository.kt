package com.example.tianfu.data

import com.example.tianfu.network.api.ApiService
import com.example.tianfu.network.model.ApiResponse
import com.example.tianfu.network.model.LoginResponseDto
import com.example.tianfu.network.model.UserDto

/**
 * 负责登录、注册等认证相关的数据访问，
 * 对上层隐藏具体的网络实现细节。
 */
class AuthRepository(
    private val apiService: ApiService,
) {
    suspend fun login(account: String, password: String): ApiResponse<LoginResponseDto> {
        // 使用兼容 Spring Boot 表单登录的 Ktor 接口
        return apiService.legacyLogin(account, password)
    }

    suspend fun register(user: UserDto): ApiResponse<Any> {
        return apiService.legacyRegister(user)
    }
}

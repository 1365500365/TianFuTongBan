package com.example.tianfu.network

import com.example.tianfu.network.model.UserDto

// 简单的内存会话管理，用于在当前应用会话内共享登录用户和 token
object AuthManager {
    @Volatile
    var token: String? = null

    @Volatile
    var user: UserDto? = null
}

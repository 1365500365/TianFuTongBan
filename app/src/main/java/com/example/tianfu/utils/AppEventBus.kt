package com.example.tianfu.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * 全局事件总线，用于跨组件通信
 */
object AppEventBus {

    // 强制退出登录事件
    private val _forceLogoutEvent = MutableSharedFlow<String>()
    val forceLogoutEvent = _forceLogoutEvent.asSharedFlow()

    /**
     * 触发强制退出登录
     * @param reason 退出原因，用于 UI 提示（例如："登录已过期，请重新登录"）
     */
    suspend fun emitForceLogout(reason: String) {
        _forceLogoutEvent.emit(reason)
    }
}
package com.example.tianfu.config

import com.example.tianfu.network.model.UserDto
import com.example.tianfu.utils.DataStoreManager
import kotlinx.serialization.builtins.nullable

/**
 * 全局持久化存储 Key 管理中心。
 *
 * 仅存放跨页面、跨模块共享的 Key。
 * 局部私有 Key 请定义在各自的 ViewModel 中。
 *
 * 支持的 Key 类型：
 * - [DataStoreManager.Key.StringKey] / [DataStoreManager.Key.IntKey] / [DataStoreManager.Key.BooleanKey]
 * - [DataStoreManager.Key.FloatKey] / [DataStoreManager.Key.LongKey] / [DataStoreManager.Key.DoubleKey]
 * - [DataStoreManager.Key.ComplexKey]（需提供 KSerializer，用于存储复杂对象/列表）
 *
 * 使用示例：
 * ```kotlin
 * // 写入
 * DataStoreManager.put(AppStoreKeys.AccessToken, "your_token")
 * // 读取
 * val token = DataStoreManager.get(AppStoreKeys.AccessToken)
 * // 观察变化（在 Composable / ViewModel 中）
 * DataStoreManager.observe(AppStoreKeys.IsLoggedIn).collect { ... }
 * ```
 */
object AppStoreKeys {

    // ================== 用户授权与状态 ==================

    /** 用于普通业务接口鉴权 */
    val AccessToken = DataStoreManager.Key.StringKey("access_token", "")

    /** 用于刷新过期的 AccessToken */
    val RefreshToken = DataStoreManager.Key.StringKey("refresh_token", "")

    /** 记录用户是否已登录 */
    val IsLoggedIn = DataStoreManager.Key.BooleanKey("is_logged_in", false)

    /** 记录用户最近一次成功登录的时间戳 */
    val LastLoginTime = DataStoreManager.Key.LongKey("last_login_time", 0L)

    /** 持久化登录成功后的用户信息，便于重启后恢复显示 */
    val UserInfo = DataStoreManager.Key.ComplexKey<UserDto?>(
        name = "user_info",
        default = null,
        serializer = UserDto.serializer().nullable
    )

    // ================== 业务数据缓存 ==================
    // TODO: 根据你的项目需求添加缓存 Key，示例：
    //
    // val CachedUser = DataStoreManager.Key.ComplexKey(
    //     "cached_user", null, User.serializer().nullable
    // )
    //
    // val CachedItems = DataStoreManager.Key.ComplexKey(
    //     "cached_items", emptyList(), ListSerializer(ExampleItem.serializer())
    // )
}

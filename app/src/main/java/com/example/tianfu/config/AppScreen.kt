package com.example.tianfu.config

import kotlinx.serialization.Serializable

/**
 * 应用屏幕路由定义。
 *
 * 使用密封接口 + [Serializable] 定义应用中所有可导航的屏幕，
 * 提供类型安全的导航，避免手写路由字符串。
 *
 * 如何添加新页面：
 * 1. 在此文件中添加新的 `data object` 或 `data class`（带参数时用 `data class`）
 * 2. 在 [appNavigation] 中添加对应的 `composable<AppScreen.Xxx> { ... }`
 * 3. 在需要跳转的地方调用 `navigator.navigate(AppScreen.Xxx)`
 */
sealed interface AppScreen {

    /**
     * 启动页 —— 应用启动时的第一个页面（品牌展示 / 初始化）
     */
    @Serializable
    data object Launch : AppScreen

    @Serializable
    data object Main : AppScreen

    /**
     * 首页 —— 应用主内容页面
     */
    @Serializable
    data object Home : AppScreen

    @Serializable
    data object Login : AppScreen

    @Serializable
    data object Register : AppScreen

    @Serializable
    data object Empty : AppScreen

    @Serializable
    data object PropertyInfo : AppScreen

    /**
     * 地区选择页
     */
    @Serializable
    data object RegionPicker : AppScreen

    /** 设置页 */
    @Serializable
    data object Setting : AppScreen

    /**
     * 详情页 —— 演示如何通过路由传递参数
     * @param itemId 列表项 ID
     */
    @Serializable
    data class Detail(val itemId: Int) : AppScreen
}
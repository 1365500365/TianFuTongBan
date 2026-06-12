package com.example.tianfu.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import com.example.tianfu.config.AppScreen

/**
 * 本地导航器组合项
 *
 * 提供全局访问导航器的能力，如果未提供导航器则会抛出错误
 * 这是使用Composition Local机制让导航器可在整个Composable层次结构中使用的关键部分
 */
val LocalComposeNavigator: ProvidableCompositionLocal<ComposeNavigator<AppScreen>> =
    compositionLocalOf {
        error(
            "未提供 AppComposeNavigator！",
        )
    }

/**
 * 当前导航器访问扩展属性
 *
 * 提供一个便捷的方式在Composable中访问当前的导航器实例
 * 使用@Composable和@ReadOnlyComposable注解确保在组合阶段安全访问
 */
val currentComposeNavigator: ComposeNavigator<AppScreen>
    @Composable
    @ReadOnlyComposable
    get() = LocalComposeNavigator.current

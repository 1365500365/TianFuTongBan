package com.example.tianfu.di

import com.example.tianfu.navigation.ComposeNavigator
import com.example.tianfu.config.AppScreen
import com.example.tianfu.navigation.AppComposeNavigator
import org.koin.dsl.module

/**
 * 导航依赖注入模块。
 *
 * 提供 [AppComposeNavigator] 单例，使导航器可在整个应用中通过 Koin 注入使用。
 */
val NavigationModule =
    module {
        single { AppComposeNavigator() }

        single<ComposeNavigator<AppScreen>> { get<AppComposeNavigator>() }
    }
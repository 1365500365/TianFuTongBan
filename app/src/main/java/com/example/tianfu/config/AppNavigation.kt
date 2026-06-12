package com.example.tianfu.config

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.tianfu.ui.screen.EmptyPage
import com.example.tianfu.ui.screen.LaunchScreen
import com.example.tianfu.ui.screen.MainScreen
import com.example.tianfu.ui.screen.SettingScreen
import com.example.tianfu.screens.LoginScreen
import com.example.tianfu.ui.screen.PropertyInfoScreen
import com.example.tianfu.screens.RegisterScreen
import com.example.tianfu.ui.screen.RegionPickerScreen

/**
 * 应用导航图定义。
 *
 * 在此函数中注册所有页面路由与对应的 Composable。
 * 每当在 [AppScreen] 中新增路由后，需要在此处添加对应的 `composable` 注册。
 *
 * 带参数的页面使用 `backStackEntry.toRoute<AppScreen.Xxx>()` 提取参数。
 */
fun NavGraphBuilder.appNavigation() {

    // 启动页
    composable<AppScreen.Launch> {
        LaunchScreen()
    }

    // 主页面
    composable<AppScreen.Main> {
        MainScreen()
    }

    // 登录
    composable<AppScreen.Login> {
        LoginScreen()
    }

    // 注册
    composable<AppScreen.Register> {
        RegisterScreen()
    }

    // 房产信息查询
    composable<AppScreen.PropertyInfo> {
        PropertyInfoScreen()
    }

    // 地区选择
    composable<AppScreen.RegionPicker> {
        RegionPickerScreen()
    }

    // 设置
    composable<AppScreen.Setting> {
        SettingScreen()
    }

    // 空页面
    composable<AppScreen.Empty> {
        EmptyPage()
    }
}

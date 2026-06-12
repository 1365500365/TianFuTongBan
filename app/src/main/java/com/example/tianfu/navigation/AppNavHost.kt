package com.example.tianfu.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.tianfu.config.AppScreen
import com.example.tianfu.config.appNavigation

/**
 * 设置导航主机，管理应用的导航逻辑。
 *
 * @param navHostController 用于控制导航的控制器。
 */
@Composable
fun AppNavHost(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = AppScreen.Launch,
    ) {
        appNavigation()
    }
}

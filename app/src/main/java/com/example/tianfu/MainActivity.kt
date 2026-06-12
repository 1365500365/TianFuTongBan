package com.example.tianfu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.tianfu.navigation.AppNavHost
import com.example.tianfu.navigation.LocalComposeNavigator
import com.example.tianfu.navigation.AppComposeNavigator
import com.example.tianfu.theme.AppTheme
import org.koin.java.KoinJavaComponent.inject
import kotlin.getValue
import kotlin.jvm.java

/**
 * 应用主 Activity，承载 Compose UI 树。
 *
 * 职责：
 * - 通过 [CompositionLocalProvider] 提供全局 [AppComposeNavigator]
 * - 设置 [AppTheme] 主题
 * - 初始化 [AppNavHost] 导航主机
 */
class MainActivity : ComponentActivity() {
    val composeNavigator: AppComposeNavigator by inject(
        clazz = AppComposeNavigator::class.java,
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(
                LocalComposeNavigator provides composeNavigator,
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AppTheme {
                        val navHostController = rememberNavController()

                        LaunchedEffect(Unit) {
                            composeNavigator.handleNavigationCommands(navHostController)
                        }

                        AppNavHost(navHostController = navHostController)
                    }
                }
            }
        }
    }
}
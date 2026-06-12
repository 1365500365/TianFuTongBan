package com.example.tianfu.ui.screen

import androidx.compose.runtime.Composable
import com.example.tianfu.navigation.currentComposeNavigator
import com.example.tianfu.ui.component.SecondaryPageTemplate

@Composable
fun EmptyPage() {
    val navigator = currentComposeNavigator
    SecondaryPageTemplate(
        title = "错误提示",
        loadingTitle = "加载中...",
        simulateLoadingTime = 2000L,
        onBackClick = {
            navigator.navigateUp()
        },
    )
}

@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.tianfu.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId

/**
 * 应用主题的组合函数，用于设置应用的整体主题样式。
 *
 * @param baseScreenWidth 基础屏幕宽度，用于自定义密度设置。
 * @param content 主题内的内容组合函数。
 */
@Composable
fun AppTheme(
    baseScreenWidth: Int = 375,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider {
        CustomDensityProvider(
            baseScreenWidth = baseScreenWidth,
        ) {
            Box(
                modifier = Modifier
                    .semantics { testTagsAsResourceId = true },
            ) {
                content()
            }
        }
    }
}
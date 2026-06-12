package com.example.tianfu.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.Density

/**
 * 提供自定义的屏幕密度和字体缩放比例的 Composable 函数
 *
 * 此函数用于提供自定义的屏幕密度和字体缩放比例，以便在不同的设备上实现一致的 UI 布局和字体大小
 * 它使用 CompositionLocalProvider 来提供 Density，
 * 以便在 Composable 函数树中任何地方都可以访问此自定义的 Density
 *
 * @param baseScreenWidth  基准屏幕宽度（单位：像素）用于计算自定义密度的参考宽度
 * @param content  需要使用自定义密度的 Composable 内容
 */
@Composable
fun CustomDensityProvider(
    baseScreenWidth: Int = 400,
    content: @Composable () -> Unit,
) {

    val displayMetrics = LocalResources.current.displayMetrics
    // 获取当前的字体缩放比例
    val currentFontScale: Float = LocalDensity.current.fontScale
    // 获取屏幕的实际宽度像素
    val actualScreenWidth: Int = displayMetrics.widthPixels

    // 使用 CompositionLocalProvider 提供自定义的密度
    CompositionLocalProvider(
        // 提供自定义的密度
        LocalDensity provides
                Density(
                    // 根据实际屏幕宽度和基准屏幕宽度计算自定义密度
                    density = actualScreenWidth / baseScreenWidth.toFloat(),
                    // 使用当前的字体缩放比例
                    fontScale = currentFontScale,
                ),
    ) {
        // 渲染传入的 Composable 内容
        content()
    }
}

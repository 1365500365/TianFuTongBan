package com.example.tianfu.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.tianfu.navigation.currentComposeNavigator

/**
 * 通用二级页面：仅展示一张截图（不实现内部布局）。
 *
 * - 截图按宽度铺满，长图可上下滚动查看。
 * - 在图片左上角原「返回」按钮位置盖一个透明热区，点击返回上一页。
 *
 * @param imageRes 要展示的截图 drawable 资源 id
 */
@Composable
fun ScreenshotScreen(@DrawableRes imageRes: Int) {
    val navigator = currentComposeNavigator

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            // 让内容（含左上角返回热区）下移到系统状态栏之下，
            // 否则裁掉状态栏后图片顶部的「返回」会被系统状态栏遮挡且热区不可点。
            .statusBarsPadding()
    ) {
        // 截图原始宽度基准为 1080px，返回按钮约位于左上角 (0..210, 0..125) 区域。
        val backWidth = maxWidth * (210f / 1080f)
        val backHeight = maxWidth * (125f / 1080f)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )

                // 透明返回热区，覆盖在图片左上角原「返回」按钮位置
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .width(backWidth)
                        .height(backHeight)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { navigator.navigateUp() }
                )
            }
        }
    }
}

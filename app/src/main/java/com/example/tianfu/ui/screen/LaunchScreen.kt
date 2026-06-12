package com.example.tianfu.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tianfu.R
import com.example.tianfu.config.AppScreen
import com.example.tianfu.navigation.currentComposeNavigator
import kotlinx.coroutines.delay

/**
 * 启动页 Screen。
 *
 * 演示如何：
 * 1. 显示应用品牌/启动画面
 * 2. 使用 [LaunchedEffect] 延时后自动跳转
 * 3. 使用 `navigateAndClearBackStack` 清除回退栈
 *
 */
@Preview
@Composable
fun LaunchScreen() {
    val navigator = currentComposeNavigator
    var countdown by remember { mutableIntStateOf(3) }
    var isFinished by remember { mutableStateOf(false) }

    // 倒计时效果
    LaunchedEffect(countdown) {
        if (countdown > 0 && !isFinished) {
            delay(1000L)
            countdown--
        } else if (!isFinished) {
            navigator.navigateAndFinish(AppScreen.Main)
            isFinished = true
        }
    }

    // 处理返回键，防止用户在启动页退出应用
    DisposableEffect(Unit) {
        onDispose {}
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 图片背景
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(R.drawable.splsh_bg),
            contentScale = ContentScale.FillWidth,
            contentDescription = null
        )

        // 右上角倒计时胶囊
        Box(
            modifier = Modifier
                .padding(top = 40.dp, end = 16.dp)
                .height(36.dp)
                .align(Alignment.TopEnd)
                .background(
                    color = Color.Black.copy(alpha = 0.5f), // 半透明黑色背景
                    shape = RoundedCornerShape(18.dp)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    // 点击胶囊立即跳转
                    if (!isFinished) {
                        navigator.navigateAndFinish(AppScreen.Main)
                        isFinished = true
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (countdown > 0) "跳过 ${countdown}s" else "跳过",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 16.sp,
                color = Color.White,
            )
        }
    }
}

package com.example.tianfu.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tianfu.R
import com.example.tianfu.navigation.currentComposeNavigator
import com.example.tianfu.theme.BackgroundGray
import kotlinx.coroutines.delay

/**
 * 通用二级页面模板
 *
 * @param title 页面标题
 * @param loadingTitle 加载中时显示的标题
 * @param needsLoading 是否需要模拟加载过程，默认为 true
 * @param simulateLoadingTime 模拟加载时间（毫秒），默认 500ms
 * @param onBackClick 自定义返回事件，不传则默认调用 navigator.navigateUp()
 * @param rightActions 右侧操作区域（如分享、客服等图标），加载完成后显示
 * @param content 页面主体内容。传入 null 则自动显示网络异常缺省页
 */
@Composable
fun SecondaryPageTemplate(
    title: String,
    loadingTitle: String? = null,
    needsLoading: Boolean = true,
    simulateLoadingTime: Long = 500L,
    onBackClick: (() -> Unit)? = null,
    backgroundColor: Color = BackgroundGray,
    rightActions: @Composable (RowScope.() -> Unit) = {},
    content: @Composable (BoxScope.() -> Unit)? = null
) {
    val navigator = currentComposeNavigator
    var isLoading by remember { mutableStateOf(needsLoading) }
    val progress = remember { Animatable(0f) }
    var refreshTrigger by remember { mutableIntStateOf(0) }

    // 兴业银行主题蓝色
    val themeBlue = Color(0xFF0080FF)

    LaunchedEffect(needsLoading, refreshTrigger) {
        if (needsLoading || refreshTrigger > 0) {
            isLoading = true
            progress.snapTo(0f)

            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = simulateLoadingTime.toInt(),
                    easing = LinearEasing
                )
            )
            delay(100)
            isLoading = false
        } else {
            isLoading = false
            progress.snapTo(1f)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 1. 顶部导航栏
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(44.dp)
                .padding(horizontal = 16.dp)
        ) {
            // 左侧返回按钮
            Icon(
                painter = painterResource(R.drawable.backbutton_txt),
                contentDescription = "返回",
                tint = Color.Unspecified,
                modifier = Modifier
                    .height(24.dp)
                    .align(Alignment.CenterStart)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (onBackClick != null) {
                            onBackClick()
                        } else {
                            navigator.navigateUp()
                        }
                    }
            )

            // 中间标题
            Text(
                text = if (isLoading) loadingTitle ?: title else title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier.align(Alignment.Center)
            )

            // 右侧动态操作区
            Box(
                modifier = Modifier.align(Alignment.CenterEnd),
                contentAlignment = Alignment.CenterEnd
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = !isLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        rightActions()
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            // 2. 加载进度条
            if (isLoading) {
                LinearProgressIndicator(
                    progress = { progress.value },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    color = themeBlue,
                    trackColor = Color(0xFFE8E8E8),
                    strokeCap = StrokeCap.Butt
                )
            }

            // 3. 页面主体内容区
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (!isLoading) {
                    if (content != null) {
                        content()
                    } else {
                        NetworkErrorState(
                            themeColor = themeBlue,
                            onRefresh = {
                                refreshTrigger++
                            },
                            onBack = {
                                if (onBackClick != null) onBackClick() else navigator.navigateUp()
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 网络异常缺省页
 */
@Composable
private fun NetworkErrorState(
    themeColor: Color,
    onRefresh: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(120.dp))

        // 错误提示图标
        Icon(
            painter = painterResource(R.drawable.weex_error),
            contentDescription = "网络错误",
            tint = Color.Unspecified,
            modifier = Modifier.size(160.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // 刷新按钮
        Button(
            onClick = onRefresh,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            colors = ButtonDefaults.buttonColors(containerColor = themeColor),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "重试", fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 返回按钮（白底蓝边）
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = themeColor),
            border = BorderStroke(1.dp, themeColor),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "返回", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}

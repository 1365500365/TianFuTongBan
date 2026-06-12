package com.example.tianfu.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 底部导航栏每个 Tab 的配置数据类
 *
 * @param title 导航项下方的文字标题（如"首页"、"财富"等）
 * @param selectedIcon 选中状态时的图标资源 ID
 * @param unselectedIcon 未选中状态时的图标资源 ID
 * @param content 点击该 Tab 时所展示的页面内容组合函数
 */
data class BottomTabItem(
    val title: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    val content: @Composable () -> Unit
)

/**
 * 自定义的底部导航页面框架
 *
 * @param selectedIndex 当前选中的 Tab 索引（由外部传入控制）
 * @param onIndexChanged 当用户点击 Tab 时触发的回调，用于通知外部更新 selectedIndex
 * @param tabItems 所有的 Tab 配置列表
 */
@Composable
fun CustomBottomNavigationLayout(
    selectedIndex: Int,
    onIndexChanged: (Int) -> Unit,
    tabItems: List<BottomTabItem>
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .navigationBarsPadding(),
        bottomBar = {
            Column {
                HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)

                // 导航栏主体
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(Color.White),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    tabItems.forEachIndexed { index, item ->
                        val isSelected = selectedIndex == index
                        val textColor = if (isSelected) Color.Black else Color(0xFF333333)

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { onIndexChanged(index) }
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // 渲染图标（因为你提供两套图，所以直接判断使用哪张即可，不需要着色）
                            Image(
                                painter = painterResource(id = if (isSelected) item.selectedIcon else item.unselectedIcon),
                                contentDescription = item.title,
                                modifier = Modifier.size(26.dp) // 控制图标大小
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            // 渲染文字
                            Text(
                                text = item.title,
                                fontSize = 10.sp,
                                color = textColor,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        // 内容区域
        Box(
            modifier = Modifier
                .padding(bottom = paddingValues.calculateBottomPadding())
                .fillMaxSize()
        ) {
            if (selectedIndex in tabItems.indices) {
                tabItems[selectedIndex].content()
            }
        }
    }
}
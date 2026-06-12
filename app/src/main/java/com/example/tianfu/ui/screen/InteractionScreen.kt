package com.example.tianfu.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tianfu.R
import com.example.tianfu.theme.BackgroundGray
import com.example.tianfu.theme.PrimaryBlue
import com.example.tianfu.theme.TextSecondary

// 组件化基础：定义统计数据实体结构
private data class InteractionStats(
    val consult: String, val consultReply: String,
    val suggest: String, val suggestReply: String,
    val complain: String, val complainReply: String
)

@Preview
@Composable
fun InteractionScreen() {
    val scrollState = rememberScrollState()
    val selectedTimeTab = remember { mutableIntStateOf(0) }

    val todayStats = InteractionStats(
        consult = "35", consultReply = "2",
        suggest = "12", suggestReply = "0",
        complain = "57", complainReply = "7"
    )
    val allStats = InteractionStats(
        consult = "130429", consultReply = "3450",
        suggest = "11138", suggestReply = "306",
        complain = "87004", complainReply = "4650"
    )

    // 根据当前选中的标签动态抓取对应的数据
    val currentStats = if (selectedTimeTab.intValue == 0) todayStats else allStats

    val repliesMock = listOf(
        "咨询" to "领取新标准的承诺书格式",
        "咨询" to "请核确认成都理工大学牛津布鲁斯...",
        "建议" to "关于恳请协调凤凰山体育公园专业足球...",
        "建议" to "成都市乡镇路边农产品售卖规范提升与...",
        "投诉" to "成都铁路运输第一法院冻结本人账户无...",
        "投诉" to "关于天鹅湖花园应急物业比选程序问题..."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .verticalScroll(scrollState)
    ) {
        // 顶部背景、Tab、及修正后的动态数据面板
        InteractionHeader(
            selectedTimeTab = selectedTimeTab.intValue,
            stats = currentStats,
            onTabClick = { selectedTimeTab.intValue = it }
        )

        // 负方向 offset 向上覆盖的内容区
        Column(
            modifier = Modifier.offset(y = (-40).dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 快捷办事区域
            QuickActionSection()

            // 我的回复
            MyReplySection()

            // 公开答复选登列表
            PublicReplySection(replies = repliesMock)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * 1. 顶部背景与动态数据统计面板
 */
@Composable
private fun InteractionHeader(
    selectedTimeTab: Int,
    stats: InteractionStats,
    onTabClick: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_interaction_top),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth
        )
        // 顶层 App Logo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.hd_logo_gr),
                contentDescription = "天府通办",
                modifier = Modifier.height(40.dp),
                contentScale = ContentScale.FillHeight
            )
        }

        // 数据面板 Card
        Column(
            modifier = Modifier
                .padding(top = 120.dp, start = 12.dp, end = 12.dp),
        ) {
            // 头部页签工具栏
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painterResource(R.drawable.hd_tj),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("数据统计(单位：件)", fontSize = 14.sp, color = Color.White)
                }
                // “今日/全部”切换模块
                Row {
                    val timeTabs = listOf("今日", "全部")
                    timeTabs.forEachIndexed { index, tab ->
                        val selected = selectedTimeTab == index
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(if (selected) Color.White.copy(alpha = 0.2f) else Color.Transparent)
                                .clickable { onTabClick(index) }
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = tab,
                                fontSize = 13.sp,
                                color = Color.White,
                                fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                thickness = 0.5.dp,
                color = Color.White.copy(alpha = 0.1f)
            )

            // 统计格网布局（咨询、建议、投诉）
            Row(
                modifier = Modifier
                    .offset(y = 16.dp)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatColumn(
                    label = "咨询", totalValue = stats.consult, replyValue = stats.consultReply,
                    modifier = Modifier.weight(1f).padding(end = 12.dp)
                )
                VerticalDividerLine()
                StatColumn(
                    label = "建议", totalValue = stats.suggest, replyValue = stats.suggestReply,
                    modifier = Modifier.weight(1f).padding(horizontal = 12.dp)
                )
                VerticalDividerLine()
                StatColumn(
                    label = "投诉", totalValue = stats.complain, replyValue = stats.complainReply,
                    modifier = Modifier.weight(1f).padding(start = 12.dp)
                )
            }
        }
    }
}

/**
 * 2. 快捷业务操作区组件
 */
@Composable
private fun QuickActionSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ActionButton("常见问题", R.drawable.ic_interaction_cjwt)
            ActionButton("我要投诉", R.drawable.ic_interaction_wyts)
            ActionButton("我要建议", R.drawable.ic_interaction_wyjy)
            ActionButton("我要咨询", R.drawable.ic_interaction_wyzx)
        }
    }
}

/**
 * 3. 我的回复卡片组件
 */
@Composable
private fun MyReplySection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Reply,
                    contentDescription = null,
                    tint = Color.DarkGray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("我的回复", fontSize = 16.sp)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { }
            ) {
                Text("去看看", fontSize = 13.sp, color = Color.Gray)
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

/**
 * 4. 答复公开列表组件
 */
@Composable
private fun PublicReplySection(replies: List<Pair<String, String>>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = null,
                        tint = Color.DarkGray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("答复公开", fontSize = 16.sp)
                        Text("最新答复选登", fontSize = 14.sp, color = TextSecondary)
                    }
                }
                Text(
                    text = "更多",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.clickable { }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(8.dp))

            replies.forEachIndexed { index, (tag, content) ->
                ReplyItem(tag, content)
                if (index < replies.size - 1) {
                    HorizontalDivider(
                        color = Color(0xFFF0F0F0),
                        thickness = 0.5.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatColumn(
    label: String,
    totalValue: String,
    replyValue: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        StatRow(label = label, value = totalValue)
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color.White.copy(alpha = 0.1f), thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(8.dp))
        StatRow(label = "回复", value = replyValue)
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 15.sp, color = Color.White)
        Text(text = value, fontSize = 19.sp, fontWeight = FontWeight.Medium, color = Color.White)
    }
}

@Composable
private fun VerticalDividerLine() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(0.5.dp)
            .background(Color.White.copy(alpha = 0.1f))
    )
}

@Composable
private fun ActionButton(title: String, iconResId: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { }
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = title,
                modifier = Modifier.size(40.dp),
                contentScale = ContentScale.Fit
            )
        }
        Text(text = title, fontSize = 12.sp, color = Color(0xFF333333))
    }
}

@Composable
private fun ReplyItem(tag: String, content: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val tagColor = when (tag) {
            "咨询" -> Color(0xFF2196F3)
            "建议" -> Color(0xFFFF9800)
            "投诉" -> Color(0xFFF44336)
            else -> Color.Gray
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(2.dp))
                .background(tagColor.copy(alpha = 0.1f))
                .padding(horizontal = 4.dp, vertical = 1.dp)
        ) {
            Text(text = tag, fontSize = 12.sp, color = tagColor, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = content,
            fontSize = 14.sp,
            color = Color.DarkGray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
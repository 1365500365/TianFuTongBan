package com.example.tianfu.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tianfu.R
import com.example.tianfu.config.AppScreen
import com.example.tianfu.config.RegionStore
import com.example.tianfu.navigation.currentComposeNavigator
import com.example.tianfu.theme.TextSecondary

@Preview
@Composable
fun AffairsScreen() {
    val navigator = currentComposeNavigator
    val region = RegionStore.currentRegion
    val scrollState = rememberScrollState()
    val selectedTab = remember { mutableIntStateOf(0) }
    val showMoreThemes = remember { mutableStateOf(false) }
    val showMoreDepts = remember { mutableStateOf(false) }

    // 主题数据（每行3个，未展开3行即9个）
    val personalThemes = listOf(
        "设立变更", "优待抚恤", "旅游观光",
        "文化体育", "医疗卫生", "公共安全",
        "环保绿化", "就业创业", "公用事业",
        "社会保障", "抵押质押", "住房保障",
        "证件办理", "出境入境", "民族宗教",
        "职业资格", "司法公证", "生育收养",
        "准营准办", "死亡殡葬", "知识产权",
        "教育科研", "户籍办理", "其他"
    )
    val corporateThemes = listOf(
        "法人注销", "资质认证", "交通运输",
        "文体教育", "抵押质押", "涉外服务",
        "农林牧渔", "商务贸易", "国土和规划建设",
        "档案文物", "司法公证", "年检年审",
        "医疗卫生", "水务气象", "准营准办",
        "质量技术", "投资审批", "民族宗教",
        "科技创新", "招标拍卖", "公安消防",
        "其他"
    )

    // 部门数据（每行2个，未展开5行即5对）
    val personalDepts = listOf(
        "市发改委" to "市教育局",
        "市科技局" to "市民宗局",
        "市公安局" to "市民政局",
        "市司法局" to "市财政局",
        "市人社局" to "市住建局",
        // 展开后显示的数据
        "市医保局" to "市商务局",
        "市生态环境局" to "市运输局",
        "市文广旅局" to "市卫健委"
    )
    val corporateDepts = listOf(
        "市发改委" to "市教育局",
        "市科技局" to "市民宗局",
        "市公安局" to "市民政局",
        "市司法局" to "市住建局",
        "市人社局" to "市商务局",
        // 展开后显示的数据
        "市财政局" to "市工商局",
        "市质监局" to "市安监局",
        "市税务局" to "市经信局"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {
        // 固定在顶部的导航栏
        AffairsTopHeader()

        // 业务内容滚动区域
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            Box {
                // Banner
                AffairsBanner()
                // Search bar
                Box(
                    modifier = Modifier
                        .offset(y = (103).dp)
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp)
                        .height(38.dp)
                        .shadow(
                            2.dp,
                            RoundedCornerShape(4.dp),
                            ambientColor = Color.Black.copy(alpha = 0.05f),
                        )
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(4.dp))
                        .padding(horizontal = 15.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.img_nav_search),
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("请输入搜索关键词", color = Color(0xFFCECCCD), fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 个人办事 / 法人办事 tabs
            Box(
                modifier = Modifier
                    .width(210.dp)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(22.dp))
                        .border(1.dp, Color(0xFF3D8BFF), RoundedCornerShape(22.dp))
                        .background(Color.White)
                ) {
                    val tabTitles = listOf("个人办事", "法人办事")
                    tabTitles.forEachIndexed { index, title ->
                        val selected = selectedTab.intValue == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedTab.intValue = index
                                    showMoreThemes.value = false
                                    showMoreDepts.value = false
                                }
                                .background(if (selected) Color(0xFF007AE7) else Color.Transparent)
                                .padding(vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                color = if (selected) Color.White else Color(0xFF666666),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 主题分类
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "主题分类",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))

                val themes = if (selectedTab.intValue == 0) personalThemes else corporateThemes
                // 🛠️ 优化点：未展开状态展示 3 行（每行3个，所以 take 9 个）
                val displayThemes = if (showMoreThemes.value) themes else themes.take(9)

                val chunkedThemes = displayThemes.chunked(3)
                chunkedThemes.forEach { rowThemes ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        rowThemes.forEach { theme ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(color = Color(0xFFF4F9FC))
                                    .clickable { }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = theme,
                                    fontSize = 13.sp,
                                    color = Color(0xFF333333),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        if (rowThemes.size < 3) {
                            repeat(3 - rowThemes.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // 主题展开/收起触发器
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showMoreThemes.value = !showMoreThemes.value }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (showMoreThemes.value) "收起" else "展开更多",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painterResource(R.drawable.ps_ic_default_arrow),
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier
                            .height(16.dp)
                            .rotate(if (showMoreThemes.value) 180f else 0f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 部门分类
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "部门分类",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))

                val departments = if (selectedTab.intValue == 0) personalDepts else corporateDepts
                // 🛠️ 优化点：未展开状态展示 5 行（每行2个，每对代表1行，所以 take(5) 组数据）
                val displayDepts = if (showMoreDepts.value) departments else departments.take(5)

                displayDepts.forEach { (left, right) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DepartmentItem(left, Modifier.weight(1f))
                        DepartmentItem(right, Modifier.weight(1f))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 部门展开/收起触发器
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showMoreDepts.value = !showMoreDepts.value }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (showMoreDepts.value) "收起" else "展开更多",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painterResource(R.drawable.ps_ic_default_arrow),
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier
                            .height(16.dp)
                            .rotate(if (showMoreDepts.value) 180f else 0f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun AffairsTopHeader() {
    val navigator = currentComposeNavigator
    val region = RegionStore.currentRegion

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_tftb),
            contentDescription = "天府通办",
            modifier = Modifier.height(28.dp),
            contentScale = ContentScale.Fit
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { navigator.navigate(AppScreen.RegionPicker) }
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.height(16.dp)
            )
            Text(
                text = region.displayText,
                fontSize = 14.sp,
                color = TextSecondary
            )
            Icon(
                painterResource(R.drawable.ps_ic_default_arrow),
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.height(16.dp)
            )
        }
    }
}

@Composable
private fun AffairsBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(0.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_affairs_banner),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun DepartmentItem(name: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .clickable { },
        shape = RoundedCornerShape(3.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F9FC)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
    }
}
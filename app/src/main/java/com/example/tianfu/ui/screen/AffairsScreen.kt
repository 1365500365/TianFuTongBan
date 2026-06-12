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

    // 部门数据（每行2个，平铺列表，奇数个时最后一格补空）
    // 个人办事·部门分类共 27 个，顺序对应 banshi 截图 25–51
    val personalDepts = listOf(
        "市发改委", "市教育局", "市科技局", "市民宗局", "市公安局",
        "市民政局", "市司法局", "市财政局", "市人社局", "市住建局",
        "市城管委", "市交通运输局", "市水务局", "市农业农村局", "市商务局",
        "市文广旅局", "市卫健委", "市市场监管局", "市体育局", "市人防办",
        "市公园城市局", "市新闻出版局", "市规划和自然资源局", "市政府侨办", "市气象局",
        "市经信局", "市消防救援支队"
    )
    val corporateDepts = listOf(
        "市发改委", "市教育局", "市科技局", "市民宗局", "市公安局", "市民政局",
        "市司法局", "市住建局", "市人社局", "市商务局", "市财政局", "市工商局",
        "市质监局", "市安监局", "市税务局", "市经信局"
    )

    // 截图映射：仅个人办事接线。主题(24)→banshi_01..24，部门(27)→banshi_25..51
    val personalThemeShots = listOf(
        R.drawable.banshi_01, R.drawable.banshi_02, R.drawable.banshi_03, R.drawable.banshi_04,
        R.drawable.banshi_05, R.drawable.banshi_06, R.drawable.banshi_07, R.drawable.banshi_08,
        R.drawable.banshi_09, R.drawable.banshi_10, R.drawable.banshi_11, R.drawable.banshi_12,
        R.drawable.banshi_13, R.drawable.banshi_14, R.drawable.banshi_15, R.drawable.banshi_16,
        R.drawable.banshi_17, R.drawable.banshi_18, R.drawable.banshi_19, R.drawable.banshi_20,
        R.drawable.banshi_21, R.drawable.banshi_22, R.drawable.banshi_23, R.drawable.banshi_24
    )
    val personalDeptShots = listOf(
        R.drawable.banshi_25, R.drawable.banshi_26, R.drawable.banshi_27, R.drawable.banshi_28,
        R.drawable.banshi_29, R.drawable.banshi_30, R.drawable.banshi_31, R.drawable.banshi_32,
        R.drawable.banshi_33, R.drawable.banshi_34, R.drawable.banshi_35, R.drawable.banshi_36,
        R.drawable.banshi_37, R.drawable.banshi_38, R.drawable.banshi_39, R.drawable.banshi_40,
        R.drawable.banshi_41, R.drawable.banshi_42, R.drawable.banshi_43, R.drawable.banshi_44,
        R.drawable.banshi_45, R.drawable.banshi_46, R.drawable.banshi_47, R.drawable.banshi_48,
        R.drawable.banshi_49, R.drawable.banshi_50, R.drawable.banshi_51
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
                val themeShots = if (selectedTab.intValue == 0) personalThemeShots else emptyList()
                // 🛠️ 优化点：未展开状态展示 3 行（每行3个，所以 take 9 个）
                val displayThemes = if (showMoreThemes.value) themes else themes.take(9)

                val chunkedThemes = displayThemes.chunked(3)
                chunkedThemes.forEachIndexed { rowIndex, rowThemes ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        rowThemes.forEachIndexed { colIndex, theme ->
                            val shot = themeShots.getOrNull(rowIndex * 3 + colIndex)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(color = Color(0xFFF4F9FC))
                                    .clickable { shot?.let { navigator.navigate(AppScreen.Screenshot(it)) } }
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
                val deptShots = if (selectedTab.intValue == 0) personalDeptShots else emptyList()
                val chunkedDepts = departments.chunked(2)
                // 🛠️ 优化点：未展开状态展示 5 行（每行2个）
                val displayDepts = if (showMoreDepts.value) chunkedDepts else chunkedDepts.take(5)

                displayDepts.forEachIndexed { rowIndex, rowDepts ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowDepts.forEachIndexed { colIndex, dept ->
                            val shot = deptShots.getOrNull(rowIndex * 2 + colIndex)
                            DepartmentItem(
                                name = dept,
                                modifier = Modifier.weight(1f),
                                onClick = { shot?.let { navigator.navigate(AppScreen.Screenshot(it)) } }
                            )
                        }
                        if (rowDepts.size < 2) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
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
private fun DepartmentItem(name: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(
        modifier = modifier
            .clickable { onClick() },
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
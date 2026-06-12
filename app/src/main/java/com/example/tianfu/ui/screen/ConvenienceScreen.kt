package com.example.tianfu.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tianfu.config.AppScreen
import com.example.tianfu.config.AppStoreKeys
import com.example.tianfu.config.RegionStore
import com.example.tianfu.navigation.currentComposeNavigator
import com.example.tianfu.theme.BackgroundGray
import com.example.tianfu.theme.PrimaryBlue
import com.example.tianfu.ui.component.ScreenTopBackground
import com.example.tianfu.ui.component.ScreenTopSearchBar
import com.example.tianfu.utils.DataStoreManager
import kotlinx.coroutines.launch

// 数据结构精简：每个分类就是一个对象
data class ConvenienceSection(val categoryName: String, val items: List<String>)

@Preview
@Composable
fun ConvenienceScreen() {
    val navigator = currentComposeNavigator
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // 1. 完全按照你截图还原的真实数据
    val categoriesData = remember { getRealConvenienceData() }
    val tabs = categoriesData.map { it.categoryName }

    // 2. 监听列表滚动：获取当前屏幕顶部第一个可见的 Item 索引，就是我们需要高亮的 Tab 索引
    val selectedTabIndex by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex.coerceIn(0, tabs.lastIndex)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        // 顶部搜索栏区域
        Box(modifier = Modifier.fillMaxWidth()) {
            ScreenTopBackground()
            val region = RegionStore.currentRegion
            ScreenTopSearchBar(
                locationText = region.displayText,
                onLocationClick = { navigator.navigate(AppScreen.RegionPicker) }
            )
        }

        // 底部内容区域
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 180.dp, start = 12.dp, end = 12.dp)
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                .background(Color.White)
        ) {
            // 横向滚动的 Tab 栏
            ConvenienceTabChips(
                tabs = tabs,
                selectedIndex = selectedTabIndex,
                onTabSelected = { index ->
                    coroutineScope.launch {
                        listState.animateScrollToItem(index)
                    }
                }
            )

            // 纵向滚动的服务列表（核心联动区）
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                // 每个分类作为一个整体 Item 渲染
                itemsIndexed(categoriesData) { _, section ->
                    ServiceSectionCard(
                        title = section.categoryName,
                        items = section.items,
                        onItemClick = { title ->
                            // 处理点击事件
                            if (title == "房产信息查询") {
                                val isLoggedIn = DataStoreManager.get(AppStoreKeys.IsLoggedIn)
                                if (isLoggedIn) {
                                    navigator.navigate(AppScreen.PropertyInfo)
                                } else {
                                    navigator.navigate(AppScreen.Login)
                                }
                            } else {
                                navigator.navigate(AppScreen.Empty)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ConvenienceTabChips(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabRowState = rememberLazyListState()

    LaunchedEffect(selectedIndex) {
        tabRowState.animateScrollToItem(selectedIndex)
    }

    LazyRow(
        state = tabRowState,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(tabs.size) { index ->
            val title = tabs[index]
            val isSelected = selectedIndex == index
            Box(
                modifier = Modifier
                    .height(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) PrimaryBlue else Color(0xFFF5F6F8)) // 未选中背景淡灰
                    .clickable { onTabSelected(index) }
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.White else Color(0xFF333333)
                )
            }
        }
    }
}

@Composable
private fun ServiceSectionCard(title: String, items: List<String>, onItemClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 10.dp, bottom = 20.dp) // 控制每个大分类之间的间距
    ) {
        // 头部标题（还原截图样式：左大标题，右小灰字）
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "查看更多",
                fontSize = 12.sp,
                color = Color(0xFF999999),
                modifier = Modifier.clickable { }
            )
        }

        // 网格内容（每行2个）
        val rows = items.chunked(2)
        rows.forEachIndexed { rowIndex, rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (rowIndex == rows.lastIndex) 0.dp else 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { item ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFF0F5FA)) // 还原截图中的浅蓝灰色背景
                            .clickable { onItemClick(item) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item,
                            fontSize = 13.sp,
                            color = Color(0xFF333333),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                // 补齐单数项
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

// 核心：基于你提供图片的真实全量数据录入
private fun getRealConvenienceData(): List<ConvenienceSection> {
    return listOf(
        ConvenienceSection("社会保障", listOf(
            "养老机构设立备案", "社保缴费（含少儿互助金）"
        )),
        ConvenienceSection("房管服务", listOf(
            "商品房预售许可进度查询", "公租房租金缴纳",
            "国有土地房屋拆迁备案查询", "房产信息查询"
        )),
        ConvenienceSection("不动产登记", listOf(
            "不动产登记-电子证明核验", "不动产登记-电子票据",
            "不动产登记-房地产数据关联"
        )),
        ConvenienceSection("公积金", listOf(
            "个人住房公积金缴存账户变动明细查询", "合作楼盘查询",
            "个人住房公积金缴存明细查询", "贷款试算表（个人缴存证明打印）"
        )),
        ConvenienceSection("交通出行", listOf(
            "天府畅行", "交通运输行政处罚查询",
            "营运车辆检测机构查询", "营运车辆检测记录查询"
        )),
        ConvenienceSection("户政治安", listOf(
            "举办攀登山峰活动审批", "临时占用公共体育场（馆）设施审批（..."
        )),
        ConvenienceSection("生活服务", listOf(
            "自来水缴费网点查询", "我要通气"
        )),
        ConvenienceSection("贷款融资", listOf(
            "农贷通", "成都市中小企业服务中心"
        )),
        ConvenienceSection("法律援助", listOf(
            "法律援助智能咨询", "法律援助机构查询"
        )),
        ConvenienceSection("教育培训", listOf(
            "进城务工人员随迁子女接受义务教育入学申..."
        )),
        ConvenienceSection("农业牧渔", listOf(
            "新建或迁建农村机电提灌站审批", "政府投资或补助的农村能源工程初步设计方...",
            "农村机电提灌站产权登记"
        )),
        ConvenienceSection("职业资格", listOf(
            "二级社会体育指导员技术等级称号认定", "二级运动员称号授予"
        )),
        ConvenienceSection("其他服务", listOf(
            "城镇污水排入排水管网许可", "文旅类社会团体成立登记前审查"
        )),
        ConvenienceSection("司法服务", listOf(
            "公证机构查询", "城市房屋所有权委托公证业务申办",
            "法定代表人权利委托公证业务申办", "机动车驾驶证公证业务申办"
        ))
    )
}
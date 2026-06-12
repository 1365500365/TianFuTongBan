package com.example.tianfu.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tianfu.R
import com.example.tianfu.config.AppScreen
import com.example.tianfu.config.RegionStore
import com.example.tianfu.navigation.currentComposeNavigator
import com.example.tianfu.theme.PrimaryBlue
import com.example.tianfu.ui.component.ScreenTopBackground
import com.example.tianfu.ui.component.ScreenTopSearchBar
import com.example.tianfu.ui.component.SectionHeader

@Preview
@Composable
fun HomeScreen(onNavigateToConvenience: () -> Unit = {}) {
    val navigator = currentComposeNavigator
    val region = RegionStore.currentRegion

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                ScreenTopBackground()
                ScreenTopSearchBar(
                    locationText = region.displayText,
                    onLocationClick = { navigator.navigate(AppScreen.RegionPicker) }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                Spacer(modifier = Modifier.height(180.dp))
                QuickActionSection()
                Spacer(modifier = Modifier.height(12.dp))
                ConvenienceServiceBanner()
                Spacer(modifier = Modifier.height(12.dp))
                HotServicesSection(onMoreClick = onNavigateToConvenience)
                Spacer(modifier = Modifier.height(12.dp))
                FeaturedThemesSection()
                Spacer(modifier = Modifier.height(12.dp))
                LifecycleServicesSection()
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Preview
@Composable
private fun QuickActionSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickActionItem("预约", R.drawable.ic_home_yy)
            QuickActionItem("办事", R.drawable.ic_home_bs)
            QuickActionItem("办件", R.drawable.ic_home_bj)
            QuickActionItem("关怀", R.drawable.ic_home_gh)
        }
    }
}

@Composable
private fun QuickActionItem(title: String, @DrawableRes iconRes: Int) {
    val navigator = currentComposeNavigator
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            navigator.navigate(AppScreen.Empty)
        }
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            modifier = Modifier
                .size(58.dp)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop
        )
        Text(text = title, fontSize = 13.sp, color = Color.DarkGray)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ConvenienceServiceBanner() {
    val banners = listOf(
        R.drawable.banner1,
        R.drawable.banner2,
        R.drawable.banner3,
        R.drawable.banner4,
        R.drawable.banner5,
        R.drawable.banner6
    )
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { banners.size })

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            HorizontalPager(state = pagerState) { page ->
                Image(
                    painter = painterResource(id = banners[page]),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(banners.size) { index ->
                val color = if (pagerState.currentPage == index) PrimaryBlue else Color(0xFFCED4DA)
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}

@Composable
private fun HotServicesSection(onMoreClick: () -> Unit = {}) {
    val navigator = currentComposeNavigator
    val services = remember { getMockHotServices() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(bottom = 12.dp)) {
            SectionHeader(title = "热门服务", showMore = false)
            Spacer(modifier = Modifier.height(12.dp))

            Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                for (row in 0..1) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (col in 0..3) {
                            val index = row * 4 + col
                            val shot = services[index].shot
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .width(84.dp)
                                    .clickable {
                                        when {
                                            services[index].title == "更多" -> onMoreClick()
                                            shot != null -> navigator.navigate(AppScreen.Screenshot(shot))
                                        }
                                    }
                                    .padding(vertical = 8.dp, horizontal = 2.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = services[index].iconRes),
                                    contentDescription = services[index].title,
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Text(
                                    text = services[index].title,
                                    fontSize = 12.sp,
                                    color = Color(0xFF333333),
                                    textAlign = TextAlign.Center,
                                    maxLines = 4,
                                    overflow = TextOverflow.Ellipsis,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeaturedThemesSection() {
    val navigator = currentComposeNavigator
    // 精选主题入口图 ↔ 对应二级截图（jingxuan_01..06，顺序一一对应）
    val themeImages = remember {
        listOf(
            R.drawable.home_image_1 to R.drawable.jingxuan_01,
            R.drawable.home_image_2 to R.drawable.jingxuan_02,
            R.drawable.home_image_3 to R.drawable.jingxuan_03,
            R.drawable.home_image_4 to R.drawable.jingxuan_04,
            R.drawable.home_image_5 to R.drawable.jingxuan_05,
            R.drawable.home_image_6 to R.drawable.jingxuan_06,
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(bottom = 16.dp)) {
            SectionHeader(title = "精选主题", showMore = true)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                themeImages.forEach { (imgRes, shotRes) ->
                    Image(
                        painter = painterResource(id = imgRes),
                        contentDescription = null,
                        modifier = Modifier
                            .width(100.dp)
                            .clickable {
                                navigator.navigate(AppScreen.Screenshot(shotRes))
                            },
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
        }
    }
}

@Composable
private fun LifecycleServicesSection() {
    val lifecycleCategories = remember { getMockLifecycleCategories() }
    var selectedCategory by remember { mutableIntStateOf(0) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            SectionHeader(title = "生命周期服务", showMore = true)
            Spacer(modifier = Modifier.height(16.dp))

            // Category tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                lifecycleCategories.forEachIndexed { index, category ->
                    val isSelected = index == selectedCategory
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { selectedCategory = index }
                    ) {
                        Text(
                            text = category.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) PrimaryBlue else Color(0xFF333333)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(PrimaryBlue)
                                    .padding(horizontal = 8.dp, vertical = 3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "共${category.count}个事项",
                                    fontSize = 10.sp,
                                    color = Color.White
                                )
                            }
                        } else {
                            Text(
                                text = "共${category.count}个事项",
                                fontSize = 10.sp,
                                color = Color(0xFF999999)
                            )
                        }
                    }
                }
            }

            // Content card with background image
            val currentCategory = lifecycleCategories[selectedCategory]
            Box(
                modifier = Modifier.padding(12.dp)
            ) {
                Image(
                    painter = painterResource(id = currentCategory.imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxWidth(0.6f)
                        .padding(end = 12.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    currentCategory.items.filter { it.isNotEmpty() }.forEach { item ->
                        Spacer(Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color.White.copy(alpha = 0.7f))
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = item,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF333333),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.width(130.dp)
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

private data class LifecycleCategory(
    val name: String,
    val count: Int,
    val imageRes: Int,
    val items: List<String>
)

private data class HotService(
    val title: String,
    @DrawableRes val iconRes: Int,
    @DrawableRes val shot: Int? = null
)

private fun getMockHotServices() = listOf(
    HotService("居民服务一件事", R.drawable.ic_home_jmfwyjs, R.drawable.sec_28),
    HotService("一码检合", R.drawable.ic_home_ymjc),
    HotService("专项信用报告（有无违法违纪记录证明版）", R.drawable.ic_home_zxxybg, R.drawable.sec_29),
    HotService("市场主体登记注册", R.drawable.ic_home_scztdjzc, R.drawable.sec_30),
    HotService("成都市用水报装", R.drawable.ic_home_cdsysbz, R.drawable.sec_31),
    HotService("个人住房公积金贷存账户变动明细查询", R.drawable.ic_home_grzfgjj),
    HotService("企业登记档案查询", R.drawable.ic_home_qydjdacx, R.drawable.sec_01),
    HotService("更多", R.drawable.ic_home_more)
)

private fun getMockLifecycleCategories() = listOf(
    LifecycleCategory("出生收养", 5, R.drawable.cdcsbg, listOf("华侨及居住在香港、澳门、台…", "《出生医学证明》首次签发", "计划生育医疗费支付", "生育医疗费支付")),
    LifecycleCategory("学习教育", 24, R.drawable.cdxxbg, listOf("四川省中等职业学校毕业生学…", "法律职业资格考试业务咨询", "盲人保健按摩培训申请", "学历公证（指引）")),
    LifecycleCategory("就业创业", 18, R.drawable.cdjybg, listOf("创业专家咨询", "就业困难人员灵活就业社保补…", "就业困难人员认定", "未就业随军配偶养老保险关系…")),
    LifecycleCategory("职业资格", 68, R.drawable.cdzyzgbg, listOf("律师执业注销许可", "法律职业资格考试业务咨询", "医疗机构执业审批（新办）（…", "医疗机构执业变更（承诺件…")),
    LifecycleCategory("工作求职", 15, R.drawable.cdgzbg, listOf("公职律师工作证颁发", "外国人来华工作许可变更审…", "外国人来华工作许可注销审…", "公司律师工作证颁发")),
    LifecycleCategory("社保就医", 8, R.drawable.cdsbjybg, listOf("失业保险金申领", "高校毕业生社保补贴申领", "暂停养老保险待遇申请", "工伤保险定期待遇领取资格认证")),
    LifecycleCategory("婚姻服务", 4, R.drawable.cdhybg, listOf("计划生育医疗费支付", "生育医疗费支付", "", "")),
    LifecycleCategory("车辆运输", 8, R.drawable.cdclbg, listOf("机动车抵押登记", "机动车注销登记", "非机动车登记", "机动车补领、换领检验合格标志")),
    LifecycleCategory("住房置业", 8, R.drawable.cdzfzybg, listOf("建造、翻建、大修自住住房（…", "购买本市新建商品住房（成…", "偿还自住住房贷款本息提取（…", "提前还清住房公积金贷款")),
    LifecycleCategory("社会救助", 8, R.drawable.cdshjzbg, listOf("人民调解员因从事工作致伤残…", "城市生活无着流浪乞讨人员救助", "残疾人教育资助申请", "学生资助政策咨询")),
    LifecycleCategory("退休养老", 8, R.drawable.cdtxylbg, listOf("企业职工基本养老保险与城乡…", "暂停养老保险待遇申请", "未就业随军配偶养老保险关系…", "多重养老保险关系个人账户退费"))
)
package com.example.tianfu.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tianfu.R
import com.example.tianfu.network.AuthManager
import com.example.tianfu.network.model.UserDto
import com.example.tianfu.config.AppStoreKeys
import com.example.tianfu.utils.DataStoreManager
import com.example.tianfu.theme.BackgroundGray
import com.example.tianfu.theme.TextSecondary
import com.example.tianfu.config.AppScreen
import com.example.tianfu.navigation.currentComposeNavigator

@Composable
fun ProfileScreen(onLoginClick: () -> Unit) {
    val scrollState = rememberScrollState()
    val selectedSubscriptionTab = remember { mutableIntStateOf(0) }
    val navigator = currentComposeNavigator

    // 先尝试用内存中的用户信息；如果为空，在进入页面时从持久化里恢复
    var currentUser by remember { mutableStateOf<UserDto?>(AuthManager.user) }

    LaunchedEffect(Unit) {
        if (currentUser == null) {
            val savedUser = DataStoreManager.get(AppStoreKeys.UserInfo)
            currentUser = savedUser
            AuthManager.user = savedUser
        }
    }

    val realName = currentUser?.realName

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        // 顶部背景图与设置按钮
        ProfileTopHeader(onSettingsClick = { navigator.navigate(AppScreen.Setting) })

        Column(
            modifier = Modifier.offset(y = (-24).dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 登录状态/用户信息卡片
            ProfileLoginCard(onLoginClick, realName)

            // 电子证明模块
            ECertificateSection()

            // 我的订阅模块
            MySubscriptionSection(selectedSubscriptionTab.intValue) {
                selectedSubscriptionTab.intValue = it
            }

            // 我的足迹模块
            MyFootprintSection()
        }
    }
}

/**
 * 名字脱敏规则：张三丰 -> *三丰
 */
private fun maskName(name: String): String {
    if (name.isBlank()) return ""
    return if (name.length <= 1) "*" else "*${name.drop(1)}"
}

/**
 * 1. 顶部背景与工具栏
 */
@Composable
private fun ProfileTopHeader(onSettingsClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.bg_profile_top),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.msg_setting),
                contentDescription = "设置",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onSettingsClick() }
            )
        }
    }
}

/**
 * 2. 用户登录/认证状态卡片
 */
@Composable
private fun ProfileLoginCard(onLoginClick: () -> Unit, realName: String?) {
    val isLoggedIn = !realName.isNullOrBlank()
    val greetingText = if (isLoggedIn) "您好，${maskName(realName)}" else "您好，请登录"
    val statusText = if (isLoggedIn) "身份认证" else "登录后认证"

    // 🛠️ 优化点：直接给 Card 配置白颜色与圆角，剔除里面冗余的 Box 夹层
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            // 用户基本信息区域
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLoginClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                val avatarResId = if (isLoggedIn) {
                    R.drawable.img_user_head_login
                } else {
                    R.drawable.img_user_head_unlogin
                }

                Image(
                    painter = painterResource(id = avatarResId),
                    contentDescription = "头像",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = greetingText,
                        color = Color(0xFF222222),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(color = Color(0xFFF0F4FF), shape = RoundedCornerShape(50.dp))
                            .padding(horizontal = 10.dp, vertical = 2.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.wd_rz),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = statusText,
                            color = Color(0xFF4A6BBA),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                Image(
                    painter = painterResource(id = R.drawable.wd_right),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }

            HorizontalDivider(
                color = Color(0xFFF2F2F2),
                thickness = 0.8.dp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // 快捷入口网格
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val quickNavigator = currentComposeNavigator
                ProfileQuickLink("我的办件", R.drawable.ic_mine_wdbj) {
                    quickNavigator.navigate(AppScreen.Screenshot(R.drawable.page_wode_banjian))
                }
                ProfileQuickLink("我的认证", R.drawable.ic_mine_wdrz) {
                    quickNavigator.navigate(AppScreen.Empty)
                }
                ProfileQuickLink("我的材料", R.drawable.ic_mine_wdcl) {
                    quickNavigator.navigate(AppScreen.Screenshot(R.drawable.page_wode_cailiao))
                }
            }
        }
    }
}

/**
 * 2.1 快捷链接子项组件
 */
@Composable
private fun ProfileQuickLink(title: String, iconResId: Int, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = title,
            modifier = Modifier.size(56.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = title,
            fontSize = 12.sp,
            color = Color(0xFF333333)
        )
    }
}

/**
 * 3. 电子证明模块
 */
@Composable
private fun ECertificateSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "电子证明",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = "全部 >",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    modifier = Modifier.clickable { }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 电子社保卡内嵌卡片
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(95.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF1976D2), Color(0xFF2196F3))
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    Text(
                        text = "电子社保卡",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "享受医保结算缴费，待遇领取等功能",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

/**
 * 4. 我的订阅模块
 */
@Composable
private fun MySubscriptionSection(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val navigator = currentComposeNavigator
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "我的订阅",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val tabs = listOf("服务", "事项")
                    tabs.forEachIndexed { index, tab ->
                        val isSelected = selectedTab == index
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) Color(0xFF367BFF) else Color(0xFFF5F5F5))
                                .clickable { onTabSelected(index) }
                                .padding(horizontal = 14.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = tab,
                                fontSize = 12.sp,
                                color = if (isSelected) Color.White else Color(0xFF666666),
                                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 缺省空状态面板：选中「服务」(tab 0)时点击跳「我的服务」截图
            Column(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .background(Color(0xFFF8F9FA), shape = RoundedCornerShape(8.dp))
                    .then(
                        if (selectedTab == 0) Modifier.clickable {
                            navigator.navigate(AppScreen.Screenshot(R.drawable.page_wode_fuwu))
                        } else Modifier
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.wd_add),
                    contentDescription = "添加",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "您还没有添加订阅服务，立即订阅",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

/**
 * 5. 我的足迹模块
 */
@Composable
private fun MyFootprintSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF79A9FF), Color(0xFF46C0F6))
                    )
                )
                .clickable { }
                .padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "我的足迹",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "去看看",
                        fontSize = 13.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Image(
                        painter = painterResource(id = R.drawable.wd_right),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenLoggedInPreview() {
    AuthManager.user = UserDto(
        realName = "张三丰",
        idType = "身份证",
        idNumber = "510100199001011234",
        phone = "13800138000"
    )
    ProfileScreen(onLoginClick = {})
}
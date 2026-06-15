package com.example.tianfu.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tianfu.R
import com.example.tianfu.navigation.currentComposeNavigator
import com.example.tianfu.theme.PrimaryBlue

/**
 * 搜索页：1:1 还原设计稿。
 *
 * - 顶部：左侧「返回」、居中标题「搜索」。
 * - 灰色圆角搜索框（放大镜 + 占位「搜索」）+ 右侧「取消」按钮返回。
 * - 输入框聚焦时框内右侧出现清空（⊗）按钮，点击清空文本。
 * - 「热门搜索」标题 + 标签（社保、医保），点击填入搜索框。
 */
@Composable
fun SearchScreen() {
    val navigator = currentComposeNavigator
    val keyboardController = LocalSoftwareKeyboardController.current

    var query by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    // 是否已发起过搜索（决定展示「热门搜索」还是搜索结果）
    var hasSearched by remember { mutableStateOf(false) }

    val hotKeywords = listOf("社保", "医保")

    fun submitSearch(keyword: String) {
        query = keyword
        if (keyword.isNotBlank()) {
            hasSearched = true
            keyboardController?.hide()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        // 顶部标题栏：返回 + 居中「搜索」
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { navigator.navigateUp() }
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = "返回",
                    color = PrimaryBlue,
                    fontSize = 16.sp
                )
            }

            Text(
                text = "搜索",
                color = Color(0xFF1A1A1A),
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEDEDED))

        // 搜索框 + 取消
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF2F2F4))
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = Color(0xFFB0B0B0),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(modifier = Modifier.weight(1f)) {
                    BasicTextField(
                        value = query,
                        onValueChange = {
                            query = it
                            // 重新编辑时回到「热门搜索」视图
                            hasSearched = false
                        },
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color(0xFF1A1A1A),
                            fontSize = 14.sp
                        ),
                        cursorBrush = androidx.compose.ui.graphics.SolidColor(PrimaryBlue),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { submitSearch(query) }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { isFocused = it.isFocused }
                    )
                    if (query.isEmpty()) {
                        Text(
                            text = "搜索",
                            color = Color(0xFFB0B0B0),
                            fontSize = 14.sp
                        )
                    }
                }
                // 聚焦时显示清空按钮
                if (isFocused) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Filled.Cancel,
                        contentDescription = "清空",
                        tint = Color(0xFFBDBDBD),
                        modifier = Modifier
                            .size(18.dp)
                            .clip(RoundedCornerShape(9.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { query = "" }
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "取消",
                color = Color(0xFF333333),
                fontSize = 15.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { navigator.navigateUp() }
                    .padding(horizontal = 4.dp, vertical = 4.dp)
            )
        }

        if (hasSearched && query.isNotBlank()) {
            // 搜索结果：暂无匹配内容时展示缺省空态
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier.padding(top = 120.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_empty),
                        contentDescription = null,
                        modifier = Modifier.size(140.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "暂无内容",
                        color = Color(0xFFB0B0B0),
                        fontSize = 15.sp
                    )
                }
            }
        } else {
            // 热门搜索
            Text(
                text = "热门搜索",
                color = Color(0xFF1A1A1A),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                hotKeywords.forEach { keyword ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFF4F4F4))
                            .clickable { submitSearch(keyword) }
                            .padding(horizontal = 16.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = keyword,
                            color = Color(0xFF333333),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEDEDED))
        }
    }
}

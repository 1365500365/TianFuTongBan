package com.example.tianfu.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tianfu.R
import com.example.tianfu.config.AppScreen
import com.example.tianfu.navigation.currentComposeNavigator
import com.example.tianfu.theme.PrimaryBlue
import com.example.tianfu.viewmodel.PropertyInfoViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PropertyInfoScreen(
    viewModel: PropertyInfoViewModel = koinViewModel(),
) {
    val navigator = currentComposeNavigator
    val context = LocalContext.current
    val uiState = viewModel.uiState

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.consumeMessage()
        }
    }

    val showAuthDialog = remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // 顶部标题栏：返回 + 居中标题
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navigator.navigateUp() }) {
                    Image(
                        painter = painterResource(id = R.mipmap.scca_login_back),
                        contentDescription = "返回",
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "房产信息查询",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                if (!uiState.showDetail) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "基本信息",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 姓名、证件号为只读，行政区域使用统一地区选择器（只到一级城市）
                    InfoRow(label = "姓名", value = maskName(uiState.name))
                    InfoRow(label = "证件号码", value = maskIdNumber(uiState.idNumber))
                    RegionCityRow(
                        city = uiState.city,
                        onClick = {
                            navigator.navigate(AppScreen.RegionPicker)
                        }
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = { viewModel.query() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Text(text = "查询", fontSize = 16.sp, color = Color.White)
                    }
                } else if (uiState.properties.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEDEDED))
                    Spacer(modifier = Modifier.height(12.dp))

                    PropertyInfoResultList(
                        properties = uiState.properties,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        if (showAuthDialog.value) {
            val checkedAgreement = remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    shape = CardDefaults.shape,
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "提示",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF333333)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "应用需要获取您的账号信息",
                            fontSize = 15.sp,
                            color = Color(0xFF333333),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = checkedAgreement.value,
                                onCheckedChange = { checkedAgreement.value = it }
                            )
                            Text(
                                text = buildAnnotatedString {
                                    append("我已阅读并同意")
                                    pushStyle(SpanStyle(color = Color(0xFF1890FF)))
                                    append("《用户协议》")
                                    pop()
                                    append("和")
                                    pushStyle(SpanStyle(color = Color(0xFF1890FF)))
                                    append("《隐私协议》")
                                    pop()
                                },
                                fontSize = 13.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }

                    HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEDEDED))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable {
                                    showAuthDialog.value = false
                                    navigator.navigateUp()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "拒绝授权",
                                fontSize = 16.sp,
                                color = Color(0xFF999999)
                            )
                        }

                        VerticalDivider(color = Color(0xFFEDEDED), thickness = 0.5.dp)

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable {
                                    if (checkedAgreement.value) {
                                        viewModel.loadUserFromAuth()
                                        showAuthDialog.value = false
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "同意授权",
                                fontSize = 16.sp,
                                color = Color(0xFF999999)
                            )
                        }
                    }
                }
            }
        }

        if (uiState.showNoResultDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.78f),
                    shape = CardDefaults.shape,
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "提示",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF333333)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "没有查到信息",
                            fontSize = 15.sp,
                            color = Color(0xFF333333),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEDEDED))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .clickable { viewModel.dismissNoResultDialog() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "我知道了",
                                fontSize = 16.sp,
                                color = Color(0xFF1890FF)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    editable: Boolean = false,
    onValueChange: ((String) -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF666666),
            modifier = Modifier.width(80.dp)
        )
        if (editable && onValueChange != null) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color(0xFF999999),
                    textAlign = TextAlign.End
                ),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = PrimaryBlue,
                    focusedLabelColor = Color.Transparent,
                    unfocusedLabelColor = Color.Transparent
                )
            )
        } else {
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color(0xFF999999),
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private fun maskName(name: String): String {
    if (name.isBlank()) return ""
    // 姓氏隐藏为 *，名字正常显示，例如 “张三丰” -> “*三丰”
    return if (name.length <= 1) "*" else "*" + name.drop(1)
}

private fun maskIdNumber(id: String): String {
    if (id.length <= 4) return id
    val prefix = id.take(3)
    val suffix = id.takeLast(2)
    val stars = "*".repeat(id.length - prefix.length - suffix.length)
    return prefix + stars + suffix
}

@Composable
private fun RegionCityRow(
    city: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "行政区域",
            fontSize = 14.sp,
            color = Color(0xFF666666),
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = city,
            fontSize = 14.sp,
            color = Color(0xFF999999),
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PropertyInfoScreenPreview() {
    PropertyInfoScreen()
}

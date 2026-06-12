package com.example.tianfu.screens

import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tianfu.R
import com.example.tianfu.config.AppScreen
import com.example.tianfu.navigation.currentComposeNavigator
import com.example.tianfu.viewmodel.LoginViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
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

    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            navigator.navigateUp()
            viewModel.resetSuccess()
        }
    }

    LoginScreenContent(
        onBackClick = { navigator.navigateUp() },
        onRegisterClick = { navigator.navigate(AppScreen.Register) },
        onLoginClick = { account, password, agreed ->
            viewModel.login(
                account = account,
                password = password,
                agreed = agreed
            )
        }
    )
}

@Composable
private fun LoginScreenContent(
    onBackClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: (account: String, password: String, agreed: Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        // Top bar with back and title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Image(
                    painter = painterResource(id = R.mipmap.scca_login_back),
                    contentDescription = "返回",
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "登录",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }

        HorizontalDivider(Modifier, thickness = 0.5.dp, color = Color(0xFFEDEDED))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val accountState = remember { mutableStateOf("") }
            val passwordState = remember { mutableStateOf("") }
            val agreeState = remember { mutableStateOf(true) }
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_tftb),
                contentDescription = null,
                modifier = Modifier
                    .height(72.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Tabs: personal / legal person
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "个人登录",
                    fontSize = 16.sp,
                    color = Color(0xFF1890FF),
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(32.dp))
                Text(
                    text = "法人登录",
                    fontSize = 16.sp,
                    color = Color(0xFF999999)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Phone / account input
            OutlinedTextField(
                value = accountState.value,
                onValueChange = { accountState.value = it },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.scca_icon_iphone),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                placeholder = {
                    Text("请输入手机号/居民身份证号/邮箱", maxLines = 1, fontSize = 12.sp)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password input
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.scca_icon_password),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                placeholder = {
                    Text("请输入密码（登录码）", maxLines = 1, fontSize = 12.sp)
                },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = agreeState.value,
                    onCheckedChange = { agreeState.value = it }
                )
                Text(
                    text = "我已阅读并遵守同意《用户服务协议》和《隐私协议》",
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onLoginClick(
                        accountState.value.trim(),
                        passwordState.value,
                        agreeState.value
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1890FF)),
                shape = RoundedCornerShape(22.dp)
            ) {
                Text(text = "登录", fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "立即注册",
                    fontSize = 13.sp,
                    color = Color(0xFF1890FF),
                    modifier = Modifier.clickable {
                        onRegisterClick()
                    }
                )
                Text(
                    text = "忘记账号/密码",
                    fontSize = 13.sp,
                    color = Color(0xFF666666)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "注册, 登录遇到问题? 进入帮助中心",
                fontSize = 12.sp,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "其他登录方式",
                fontSize = 12.sp,
                color = Color(0xFF999999)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFFE6F2FF), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.scca_icon_alipay),
                            contentDescription = null,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "支付宝", fontSize = 12.sp, color = Color(0xFF666666))
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreenContent(
        onBackClick = {},
        onRegisterClick = {},
        onLoginClick = { _, _, _ -> }
    )
}

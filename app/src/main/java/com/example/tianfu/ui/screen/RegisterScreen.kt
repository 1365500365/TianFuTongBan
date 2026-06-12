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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tianfu.R
import com.example.tianfu.navigation.currentComposeNavigator
import com.example.tianfu.viewmodel.RegisterViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = koinViewModel(),
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

    RegisterScreenContent(
        onBackClick = { navigator.navigateUp() },
        onRegisterClick = { realName, idNumber, phone, password, confirmPassword, agreed ->
            viewModel.register(
                realName = realName,
                idNumber = idNumber,
                phone = phone,
                password = password,
                confirmPassword = confirmPassword,
                agreed = agreed
            )
        }
    )
}

@Composable
private fun RegisterScreenContent(
    onBackClick: () -> Unit,
    onRegisterClick: (
        realName: String,
        idNumber: String,
        phone: String,
        password: String,
        confirmPassword: String,
        agreed: Boolean,
    ) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        // Top bar
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
                text = "个人账号密码注册",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }

        HorizontalDivider(Modifier, thickness = 0.5.dp, color = Color(0xFFEDEDED))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            val realNameState = remember { mutableStateOf("") }
            val idNumberState = remember { mutableStateOf("") }
            val phoneState = remember { mutableStateOf("") }
            val passwordState = remember { mutableStateOf("") }
            val confirmPasswordState = remember { mutableStateOf("") }
            val agreeState = remember { mutableStateOf(true) }

            Text(
                text = "请录入证件信息，完成实名认证",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Name
            OutlinedTextField(
                value = realNameState.value,
                onValueChange = { realNameState.value = it },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.scca_icon_person_info),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                placeholder = { Text("请输入真实姓名", fontSize = 12.sp) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ID type row (simplified as one row)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "证件类型",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = "居民身份证",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(top = 8.dp),
                thickness = 0.5.dp,
                color = Color(0xFFEDEDED)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ID number
            OutlinedTextField(
                value = idNumberState.value,
                onValueChange = { idNumberState.value = it },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.scca_icon_licence),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                placeholder = { Text("请输入证件号码", fontSize = 12.sp) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone
            OutlinedTextField(
                value = phoneState.value,
                onValueChange = { phoneState.value = it },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.scca_icon_iphone),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                placeholder = { Text("请输入手机号", fontSize = 12.sp) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password
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
                placeholder = { Text("请输入8-16位数字、小写字母及特殊字符的组合", maxLines = 1, fontSize = 12.sp) },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm password
            OutlinedTextField(
                value = confirmPasswordState.value,
                onValueChange = { confirmPasswordState.value = it },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.scca_icon_password),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                placeholder = { Text("请确认密码", fontSize = 12.sp) },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "注册遇到问题? 进入帮助中心",
                fontSize = 12.sp,
                color = Color(0xFF1890FF),
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onRegisterClick(
                        realNameState.value.trim(),
                        idNumberState.value.trim(),
                        phoneState.value.trim(),
                        passwordState.value,
                        confirmPasswordState.value,
                        agreeState.value
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1890FF)),
                shape = RoundedCornerShape(22.dp)
            ) {
                Text(text = "注册", fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = agreeState.value, onCheckedChange = { agreeState.value = it })
                Text(
                    text = "我已阅读并遵守同意《用户服务协议》和《隐私政策》",
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    RegisterScreenContent(
        onBackClick = {},
        onRegisterClick = { _, _, _, _, _, _ -> }
    )
}

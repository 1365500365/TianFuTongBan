package com.example.tianfu.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tianfu.config.AppScreen
import com.example.tianfu.config.AppStoreKeys
import com.example.tianfu.navigation.currentComposeNavigator
import com.example.tianfu.network.AuthManager
import com.example.tianfu.utils.DataStoreManager
import kotlinx.coroutines.launch

@Composable
fun SettingScreen() {
    val navigator = currentComposeNavigator
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "设置", modifier = Modifier.padding(bottom = 16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                scope.launch {
                    // 清理登录态
                    DataStoreManager.put(AppStoreKeys.AccessToken, "")
                    DataStoreManager.put(AppStoreKeys.RefreshToken, "")
                    DataStoreManager.put(AppStoreKeys.IsLoggedIn, false)
                    DataStoreManager.put(AppStoreKeys.UserInfo, null)
                    AuthManager.token = null
                    AuthManager.user = null

                    // 跳转到登录页并清空回退栈
                    navigator.navigateAndClearBackStack(AppScreen.Login)
                }
            }
        ) {
            Text(text = "退出登录")
        }

        Spacer(modifier = Modifier.padding(top = 8.dp))
    }
}

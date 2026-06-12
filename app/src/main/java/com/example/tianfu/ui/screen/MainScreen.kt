package com.example.tianfu.ui.screen

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tianfu.R
import com.example.tianfu.config.AppScreen
import com.example.tianfu.navigation.currentComposeNavigator
import com.example.tianfu.screens.ProfileScreen
import com.example.tianfu.ui.component.BottomTabItem
import com.example.tianfu.ui.component.CustomBottomNavigationLayout
import com.example.tianfu.ui.component.CustomDialog
import com.example.tianfu.ui.viewmodel.MainViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel()
) {
    val navigator = currentComposeNavigator
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    val activity = LocalActivity.current

    val tabs = listOf(
        BottomTabItem(
            title = "首页",
            selectedIcon = R.drawable.home_tab_select,
            unselectedIcon = R.drawable.home_tab_unselect,
            content = {
                HomeScreen()
            }
        ),
        BottomTabItem(
            title = "便民",
            selectedIcon = R.drawable.service_tab_select,
            unselectedIcon = R.drawable.service_tab_unselect,
            content = {
                ConvenienceScreen()
            }
        ),

        BottomTabItem(
            title = "办事",
            selectedIcon = R.drawable.matter_tab_select,
            unselectedIcon = R.drawable.matter_tab_unselect,
            content = {
                AffairsScreen()
            }
        ),
        BottomTabItem(
            title = "互动",
            selectedIcon = R.drawable.interaction_tab_select,
            unselectedIcon = R.drawable.interaction_tab_un_select,
            content = {
                InteractionScreen()
            }
        ),
        BottomTabItem(
            title = "我的",
            selectedIcon = R.drawable.mine_tab_select,
            unselectedIcon = R.drawable.mine_tab_unselect,
            content = {
                ProfileScreen(onLoginClick = {
                    navigator.navigate(AppScreen.Login)
                })
            }
        )
    )

    CustomBottomNavigationLayout(
        selectedIndex = selectedIndex,
        onIndexChanged = { newIndex ->
            selectedIndex = newIndex
        },
        tabItems = tabs
    )

    if (uiState.showDisclaimerDialog) {
        CustomDialog(
            title = "免责声明",
            content = "本应用仅供娱乐学习使用，不涉及任何真实数据查询，请勿用于任何非法或商业用途。继续使用即表示您同意本条款。",
            cancelText = "退出",
            confirmText = "我已了解",
            onCancel = { activity?.finish() },
            onConfirm = { viewModel.agreeDisclaimer() }
        )
    }

    if (uiState.showExitDialog) {
        CustomDialog(
            title = "温馨提示",
            content = "您确认要退出应用？",
            cancelText = "取消",
            confirmText = "确认",
            onCancel = { viewModel.toggleExitDialog(false) },
            onConfirm = {
                viewModel.toggleExitDialog(false)
                activity?.finish()
            }
        )
    }
}
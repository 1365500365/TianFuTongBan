package com.example.tianfu.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.tianfu.ui.base.BaseViewModel
import com.example.tianfu.ui.state.MainUiState
import com.example.tianfu.utils.DataStoreManager
import kotlinx.coroutines.launch

class MainViewModel() : BaseViewModel<MainUiState>(MainUiState()) {

    companion object {
        val IsFirstLaunchKey = DataStoreManager.Key.BooleanKey("is_first_launch", true)
    }

    init {
        // 1. 初始化时获取首次启动状态
        val isFirstLaunch = DataStoreManager.get(IsFirstLaunchKey)
        if (isFirstLaunch) {
            updateState { copy(showDisclaimerDialog = true) }
        }
    }

    fun agreeDisclaimer() {
        viewModelScope.launch {
            DataStoreManager.put(IsFirstLaunchKey, false)
            updateState { copy(showDisclaimerDialog = false) }
        }
    }

    fun toggleExitDialog(show: Boolean) {
        updateState { copy(showExitDialog = show) }
    }
}
package com.example.tianfu.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.tianfu.network.api.ApiService
import com.example.tianfu.ui.state.ExampleUiState
import com.example.tianfu.ui.base.BaseViewModel
import kotlinx.coroutines.launch

/**
 * 示例 ViewModel。
 *
 * 演示如何：
 * 1. 继承 [BaseViewModel] 并指定 UiState 类型
 * 2. 通过构造函数注入 [ApiService]（由 Koin 提供）
 * 3. 使用 [updateState] 更新 UI 状态
 * 4. 在 viewModelScope 中发起网络请求
 *
 * 在 Screen 中使用：
 * ```kotlin
 * val viewModel: ExampleViewModel = koinViewModel()
 * val uiState by viewModel.uiState.collectAsStateWithLifecycle()
 * ```
 *
 * TODO: 将此类重命名并修改为你的业务逻辑
 */
class ExampleViewModel(
    private val apiService: ApiService
) : BaseViewModel<ExampleUiState>(ExampleUiState()) {

    init {
        loadItems()
    }

    /**
     * 加载列表数据
     */
    fun loadItems() {
        updateState { copy(isLoading = true, isError = false) }
        viewModelScope.launch {
            try {
                val response = apiService.getExampleList()
                if (response.code == 200) {
                    updateState {
                        copy(
                            items = response.data?.items ?: emptyList(),
                            isLoading = false
                        )
                    }
                } else {
                    updateState {
                        copy(isLoading = false, isError = true, errorMessage = response.msg)
                    }
                }
            } catch (e: Exception) {
                updateState {
                    copy(isLoading = false, isError = true, errorMessage = e.message)
                }
            }
        }
    }

    /**
     * 选中某个列表项
     */
    fun selectItem(itemId: Int) {
        updateState { copy(selectedItemId = itemId) }
    }
}

package com.example.tianfu.ui.state

import com.example.tianfu.network.model.ExampleItem
import com.example.tianfu.ui.base.BaseUiState

/**
 * 示例页面的 UI 状态。
 *
 * 继承 [BaseUiState] 获得通用的 isLoading / isError / errorMessage 字段。
 * 在此基础上添加页面特有的业务数据字段。
 *
 * 使用示例（在 ViewModel 中更新状态）：
 * ```kotlin
 * updateState { copy(items = newItems, isLoading = false) }
 * ```
 *
 * 使用示例（在 Composable 中观察状态）：
 * ```kotlin
 * val uiState by viewModel.uiState.collectAsStateWithLifecycle()
 * if (uiState.isLoading) { LoadingIndicator() }
 * ```
 *
 * TODO: 根据你的页面需求修改此类的字段
 */
data class ExampleUiState(
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val errorMessage: String? = null,
    val items: List<ExampleItem> = emptyList(),
    val selectedItemId: Int? = null,
) : BaseUiState

package com.example.tianfu.ui.state

import com.example.tianfu.ui.base.BaseUiState

data class MainUiState(
    val showDisclaimerDialog: Boolean = false,
    val showExitDialog: Boolean = false,

    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val errorMessage: String? = null
) : BaseUiState
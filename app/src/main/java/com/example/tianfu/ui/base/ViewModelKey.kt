package com.example.tianfu.ui.base

/**
 * ViewModel 唯一标识键。
 *
 * 用于 [ViewModelStateFlow] 中标识状态流所属的 ViewModel，防止外部非法修改。
 *
 * @property key ViewModel 的全限定类名
 */
data class ViewModelKey(
    val key: String,
)

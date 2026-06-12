package com.example.tianfu.di

import com.example.tianfu.ui.viewmodel.ExampleViewModel
import com.example.tianfu.ui.viewmodel.MainViewModel
import com.example.tianfu.viewmodel.LoginViewModel
import com.example.tianfu.viewmodel.PropertyInfoViewModel
import com.example.tianfu.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * ViewModel 依赖注入模块。
 *
 * 在此处注册所有 ViewModel，Koin 会自动管理它们的生命周期。
 * `get()` 会自动从其他模块中获取所需的依赖（如 [ApiService]）。
 *
 * 如何添加新的 ViewModel：
 * ```kotlin
 * viewModel { YourViewModel(get()) }
 * ```
 */
val ViewModelModule = module {
    // 示例 ViewModel（注入 ApiService）
    viewModel { ExampleViewModel(get()) }
    viewModel { MainViewModel() }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { PropertyInfoViewModel(get()) }

    // TODO: 在下方注册你的 ViewModel，示例：
    // viewModel { LoginViewModel(get()) }
    // viewModel { ProfileViewModel(get()) }
}
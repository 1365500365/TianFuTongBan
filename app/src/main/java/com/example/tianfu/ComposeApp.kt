package com.example.tianfu

import android.app.Application
import com.example.tianfu.di.ViewModelModule
import com.example.tianfu.di.NavigationModule
import com.example.tianfu.di.NetworkModule
import com.example.tianfu.utils.DataStoreManager
import com.tencent.mmkv.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

/**
 * 应用程序入口，负责全局初始化工作。
 *
 * 初始化内容：
 * - Koin 依赖注入框架（注册导航、网络、ViewModel 模块）
 * - [DataStoreManager] 持久化存储
 * - Timber 日志（仅 Debug 模式）
 */
class ComposeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        // 初始化 Koin 依赖注入框架
        startKoin {
            androidContext(this@ComposeApp)
            modules(
                NavigationModule,
                NetworkModule,
                ViewModelModule
            )
        }
        // 初始化 DataStoreManager
        DataStoreManager.init(this)
    }
}

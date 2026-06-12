# Android App 模板项目

基于 Jetpack Compose + Koin + Ktor 的 Android 应用快速开发模板。

## 技术栈

| 技术 | 用途 |
|---|---|
| **Jetpack Compose** | 声明式 UI 框架 |
| **Koin** | 轻量级依赖注入 |
| **Ktor Client** | HTTP 网络请求 |
| **kotlinx.serialization** | JSON 序列化/反序列化 |
| **MMKV** | 高性能键值持久化存储 |
| **Timber** | 日志输出 |
| **Coil** | 图片加载 |

## 项目结构

```
com.sample.template/
├── ComposeApp.kt                  # Application 入口（Koin 初始化、DataStore 初始化）
├── MainActivity.kt                # 主 Activity（Compose 入口、Navigator 提供）
│
├── config/                        # 高频开发配置（路由、导航图、存储 Key、全局常量）
│   ├── AppScreen.kt               #   路由定义（sealed interface + @Serializable）
│   ├── AppNavigation.kt           #   导航图注册（composable 路由映射）
│   ├── AppStoreKeys.kt            #   全局持久化存储 Key 定义
│   └── Config.kt                  #   全局配置常量（BASE_URL、超时等）
│
├── di/                            # Koin 依赖注入模块
│   ├── NavigationModule.kt        #   导航器单例
│   ├── NetworkModule.kt           #   HttpClient + ApiService（含 Token 拦截器）
│   └── ViewModelModule.kt         #   ViewModel 注册
│
├── navigation/                    # 导航底层基础设施（一般无需修改）
│   ├── AppNavHost.kt              #   NavHost 组合入口
│   ├── AppComposeNavigator.kt     #   导航器实现
│   ├── Navigator.kt               #   导航器抽象基类
│   ├── NavigationCommand.kt       #   导航命令密封类
│   ├── NavigationAnimation.kt     #   页面切换动画配置
│   └── LocalComposeNavigator.kt   #   CompositionLocal 提供导航器
│
├── network/                       # 网络层
│   ├── api/
│   │   └── ApiService.kt          #   API 接口定义（示例）
│   └── model/
│       └── Models.kt              #   数据模型（ApiResponse、业务实体示例）
│
├── theme/                         # 主题
│   ├── AppTheme.kt                #   应用主题入口
│   └── CustomDensityProvider.kt   #   屏幕适配（按设计稿基准宽度缩放）
│
├── ui/                            # UI 层
│   ├── base/                      #   基础组件（一般无需修改）
│   │   ├── BaseUiState.kt         #     UI 状态基接口
│   │   ├── BaseViewModel.kt       #     ViewModel 基类（状态管理、去重、调试）
│   │   ├── ViewModelKey.kt        #     ViewModel 标识键
│   │   └── ViewModelStateFlow.kt  #     安全的 StateFlow 封装
│   ├── component/                 #   可复用组件（鼓励组件化开发）
│   │   ├── AppTopBar.kt           #     通用顶部导航栏
│   │   ├── EmptyStateView.kt      #     空状态占位视图
│   │   ├── ErrorRetryView.kt      #     错误重试视图
│   │   └── LoadingOverlay.kt      #     加载遮罩
│   ├── screen/                    #   页面（示例）
│   │   ├── LaunchScreen.kt        #     启动页
│   │   ├── HomeScreen.kt          #     首页（列表 + 状态处理）
│   │   └── DetailScreen.kt        #     详情页（路由参数 + 返回）
│   ├── state/                     #   UI 状态（示例）
│   │   └── ExampleUiState.kt
│   └── viewmodel/                 #   ViewModel（示例）
│       └── ExampleViewModel.kt
│
└── utils/                         # 底层工具类（一般无需修改）
    ├── DataStoreManager.kt        #   MMKV 封装（支持 Flow 观察、复杂对象序列化）
    ├── AppEventBus.kt             #   全局事件总线（如强制登出）
    ├── BrowserUtils.kt            #   WebView 工具
    └── LocationUtils.kt           #   定位工具
```

## 快速开始

### 1. 克隆 & 配置

1. 用 Android Studio 打开项目
2. 修改 `config/Config.kt` 中的 `BASE_URL` 为你的后端地址
3. 修改 `app/build.gradle.kts` 中的 `applicationId` 和 `namespace`
4. 修改 `res/values/strings.xml` 中的 `app_name`
5. Gradle Sync → Run

### 2. 添加新页面

**Step 1** — 在 `config/AppScreen.kt` 中定义路由：

```kotlin
@Serializable
data object Login : AppScreen

@Serializable
data class Profile(val userId: Int) : AppScreen
```

**Step 2** — 在 `config/AppNavigation.kt` 中注册 Composable：

```kotlin
composable<AppScreen.Login> {
    LoginScreen()
}

composable<AppScreen.Profile> { backStackEntry ->
    val args = backStackEntry.toRoute<AppScreen.Profile>()
    ProfileScreen(userId = args.userId)
}
```

**Step 3** — 创建对应的 Screen Composable：

```kotlin
@Composable
fun LoginScreen() {
    val navigator = currentComposeNavigator
    // ...
    navigator.navigate(AppScreen.Profile(userId = 1))
}
```

### 3. 添加新 ViewModel

**Step 1** — 创建 UiState（`ui/state/`）：

```kotlin
data class LoginUiState(
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val errorMessage: String? = null,
    val isLoggedIn: Boolean = false,
) : BaseUiState
```

**Step 2** — 创建 ViewModel（`ui/viewmodel/`）：

```kotlin
class LoginViewModel(
    private val apiService: ApiService
) : BaseViewModel<LoginUiState>(LoginUiState()) {

    fun login(account: String, password: String) {
        updateState { copy(isLoading = true) }
        viewModelScope.launch {
            // 调用 API ...
            updateState { copy(isLoading = false, isLoggedIn = true) }
        }
    }
}
```

**Step 3** — 在 `di/ViewModelModule.kt` 中注册：

```kotlin
viewModel { LoginViewModel(get()) }
```

### 4. 添加新 API 接口

在 `network/api/ApiService.kt` 中添加方法：

```kotlin
suspend fun getArticles(page: Int): ApiResponse<PagedData<Article>> {
    return client.get("api/articles") {
        parameter("page", page)
    }.body()
}
```

在 `network/model/Models.kt` 中添加数据模型：

```kotlin
@Serializable
data class Article(
    val id: Int,
    val title: String,
    val content: String,
)
```

## 内置功能

### Token 自动管理
`NetworkModule` 中的 Ktor 拦截器会：
- 自动在请求头注入 `Authorization: Bearer {token}`
- 遇到 401 时自动用 RefreshToken 刷新并重试
- 刷新失败时触发 `AppEventBus.forceLogoutEvent`

### 持久化存储
通过 `DataStoreManager` + `AppStoreKeys` 进行键值读写：

```kotlin
// 写入
DataStoreManager.put(AppStoreKeys.AccessToken, "token_value")
// 读取
val token = DataStoreManager.get(AppStoreKeys.AccessToken)
// 观察变化
DataStoreManager.observe(AppStoreKeys.IsLoggedIn).collect { isLoggedIn -> ... }
```

### 导航
使用类型安全的 `AppScreen` 密封接口 + `currentComposeNavigator`：

```kotlin
val navigator = currentComposeNavigator
navigator.navigate(AppScreen.Detail(itemId = 42))    // 前进
navigator.navigateUp()                                 // 返回
navigator.navigateAndClearBackStack(AppScreen.Home)    // 清栈跳转
```

## 注意事项

- 所有示例文件均带有 `TODO` 注释标记，搜索 `TODO` 即可定位需要修改的位置
- 项目中的 `Unresolved reference` 错误在 Gradle Sync 后会自动消失
- `ui/base/` 中的基类为核心基础设施，一般不需要修改

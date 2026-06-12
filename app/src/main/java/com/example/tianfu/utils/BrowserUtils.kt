package com.example.tianfu.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.core.net.toUri
import timber.log.Timber

/**
 * 浏览器相关工具类
 * 负责 WebView 的基础配置、JS 脚本注入、资源清理和外部跳转逻辑
 */
object BrowserUtils {

    /**
     * 配置 WebView 的通用设置
     */
    @SuppressLint("SetJavaScriptEnabled")
    fun initWebSettings(webView: WebView) {
        webView.settings.apply {
            // 基础交互
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true

            // 视图与缩放
            useWideViewPort = true
            loadWithOverviewMode = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false

            // 字体与布局
            textZoom = 100

            // 缓存与网络
            cacheMode = WebSettings.LOAD_DEFAULT
            // 混合内容模式 (允许 HTTPS 加载 HTTP 资源，解决部分图片不显示)
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

            // 媒体播放优化
            mediaPlaybackRequiresUserGesture = false

            // 安全性 (根据需求开启，通常加载本地文件需要)
            allowFileAccess = true

            // 追加 UserAgent (可选：方便 H5 识别 App 环境)
            userAgentString = "$userAgentString Android_iHub_App"
        }

        // 同步 Cookie
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
    }

    /**
     * 处理外部链接跳转 (核心优化)
     * 用于拦截非 HTTP/HTTPS 协议（如 alipay://, weixin://, tel: 等）
     * * @return true 表示已拦截处理（不应由 WebView 继续加载），false 表示是普通网页
     */
    fun handleExternalLink(context: Context, url: String): Boolean {
        // 1. 如果是 http/https，直接返回 false，让 WebView 自己加载
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return false
        }

        // 2. 尝试解析并跳转外部 App
        return try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            Timber.w("Browser: 无法处理外部 Scheme: $url, 原因: ${e.message}")
            true
        }
    }

    /**
     * 打开外部系统浏览器 (明确意图为打开浏览器)
     */
    fun openExternalBrowser(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            Timber.e(e, "打开外部浏览器失败")
        }
    }

    /**
     * 获取页面描述的 JS 脚本
     */
    fun getDescriptionScript(): String {
        return """
            (function() { 
                try {
                    var metaDesc = document.querySelector('meta[name="description"]'); 
                    var desc = metaDesc ? metaDesc.getAttribute('content') : null; 
                    if (!desc) { 
                      var firstParagraph = document.querySelector('body p'); 
                      desc = firstParagraph ? firstParagraph.innerText : ''; 
                    } 
                    // 截取前 100 个字符
                    return { description: desc ? desc.substring(0, 100) : '' }; 
                } catch(e) {
                    return { description: '' };
                }
            })()
        """.trimIndent()
    }

    /**
     * 彻底清理 WebView 资源
     */
    fun destroyWebView(webView: WebView?) {
        if (webView == null) return

        try {
            // 1. 先从父容器中移除 (关键！防止崩溃)
            val parent = webView.parent
            if (parent is ViewGroup) {
                parent.removeView(webView)
            }

            // 2. 清理内部状态
            webView.stopLoading()
            webView.settings.javaScriptEnabled = false
            webView.clearHistory()
            webView.removeAllViews()

            // 3. 销毁
            webView.destroy()
        } catch (e: Exception) {
            Timber.e(e, "WebView destroy error")
        }
    }
}
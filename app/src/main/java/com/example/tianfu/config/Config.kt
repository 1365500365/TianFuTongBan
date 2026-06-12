package com.example.tianfu.config

/**
 * 全局配置常量。
 *
 * 存放网络基础地址、超时时间等全局配置项。
 *
 * TODO: 修改为你项目的实际配置值
 */
object Config {

    /** 后端 API 基础地址（末尾需带 /） */
    const val BASE_URL = "http://tongban.app888.xyz/"

    /** 当前 App 的标识，用于多应用隔离（与后端 app 表的 id 对应） */
    const val APP_ID: Long = 1L

    /** 网络连接超时时间（毫秒） */
    const val CONNECT_TIMEOUT = 30_000L
}
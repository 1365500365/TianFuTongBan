package com.example.tianfu.utils

import android.content.Context
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import timber.log.Timber

/**
 * 基于 MMKV 的持久化存储管理器。
 *
 * 提供类型安全的 Key 定义、同步读写、响应式数据观察。
 * 使用前需在 [Application.onCreate] 中调用 [init] 完成初始化。
 *
 * @see Key 支持的数据类型定义
 * @see AppStoreKeys 全局 Key 管理中心
 */
object DataStoreManager {

    private var isInitialized = false

    // 全局 Json 配置
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    }

    // 用于通知变更的 Flow，replay = 1 确保订阅时能拿到最新状态（可选，配合 onStart 使用）
    private val keyUpdateFlow = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val mmkv: MMKV by lazy {
        if (!isInitialized) {
            throw IllegalStateException("DataStoreManager has not been initialized. Call init() in Application.")
        }
        MMKV.defaultMMKV()
    }

    /**
     * 在 Application onCreate 中调用
     */
    fun init(context: Context) {
        MMKV.initialize(context)
        isInitialized = true
    }

    // ================== Key 定义 ==================
    sealed class Key<T>(val name: String, val default: T) {
        class BooleanKey(name: String, default: Boolean = false) : Key<Boolean>(name, default)
        class IntKey(name: String, default: Int = 0) : Key<Int>(name, default)
        class StringKey(name: String, default: String = "") : Key<String>(name, default)
        class FloatKey(name: String, default: Float = 0f) : Key<Float>(name, default)
        class LongKey(name: String, default: Long = 0L) : Key<Long>(name, default)
        class DoubleKey(name: String, default: Double = 0.0) : Key<Double>(name, default)
        class ComplexKey<T>(
            name: String,
            default: T,
            val serializer: KSerializer<T>
        ) : Key<T>(name, default)
    }

    // ================== 核心 API ==================

    /**
     * 写入数据
     * 合并了原来的 write/writeSync。MMKV 极快，一般情况无需挂起，
     * 但为了兼容 Flow 发送和可能的复杂对象 JSON 序列化，保留 suspend 并在 IO 线程执行。
     */
    suspend fun <T> put(key: Key<T>, value: T) {
        // 1. 性能优化：如果值没有变，直接返回，不触发 IO 也不触发通知
        try {
            val current = get(key)
            if (current == value) return
        } catch (_: Exception) {
        }

        val keyName = key.name

        // 2. 写入操作
        runCatching {
            when (key) {
                is Key.BooleanKey -> mmkv.encode(keyName, value as Boolean)
                is Key.IntKey -> mmkv.encode(keyName, value as Int)
                is Key.StringKey -> mmkv.encode(keyName, value as String)
                is Key.FloatKey -> mmkv.encode(keyName, value as Float)
                is Key.LongKey -> mmkv.encode(keyName, value as Long)
                is Key.DoubleKey -> mmkv.encode(keyName, value as Double)
                is Key.ComplexKey -> {
                    val jsonString = json.encodeToString(key.serializer, value)
                    mmkv.encode(keyName, jsonString)
                }
            }
        }.onFailure {
            Timber.Forest.e(it, "DataStoreManager put failed: $keyName")
        }

        // 3. 发送通知
        keyUpdateFlow.emit(keyName)
    }

    /**
     * 同步获取当前值 (替代原来的 readSync)
     * 适合非 UI 逻辑中的直接读取
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: Key<T>): T {
        val keyName = key.name
        if (!mmkv.containsKey(keyName)) return key.default

        return try {
            when (key) {
                is Key.BooleanKey -> mmkv.decodeBool(keyName, key.default) as T
                is Key.IntKey -> mmkv.decodeInt(keyName, key.default) as T
                is Key.StringKey -> mmkv.decodeString(keyName, key.default) as T
                is Key.FloatKey -> mmkv.decodeFloat(keyName, key.default) as T
                is Key.LongKey -> mmkv.decodeLong(keyName, key.default) as T
                is Key.DoubleKey -> mmkv.decodeDouble(keyName, key.default) as T
                is Key.ComplexKey -> {
                    val jsonString = mmkv.decodeString(keyName, "")
                    if (jsonString.isNullOrEmpty()) key.default
                    else json.decodeFromString(key.serializer, jsonString)
                }
            }
        } catch (e: Exception) {
            Timber.Forest.e(e, "DataStoreManager get failed: $keyName")
            key.default
        }
    }

    /**
     * 监听数据变化 (替代原来的 read)
     * 适合 UI 绑定
     */
    fun <T> observe(key: Key<T>): Flow<T> {
        return keyUpdateFlow
            .filter { it == key.name }
            .onStart { emit("init") }
            .map { get(key) }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
            .conflate()
    }
}
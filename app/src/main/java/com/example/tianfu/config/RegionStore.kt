package com.example.tianfu.config

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * 全局地区选择状态。
 *
 * 所有页面的地区选择器都应读取/更新这里的值，保证显示保持一致。
 */
data class RegionSelection(
    val city: String = "成都市",
    val station: String = "市本级",
) {
    /**
     * 顶部展示用的短文案：
     * - 未选择具体站点或为“市本级”时，仅显示城市名（例如 "成都市"）
     * - 否则显示二级站点名（例如 "天府新区"）
     */
    val displayText: String
        get() = if (station.isBlank() || station == "市本级") city else station
}

object RegionStore {

    /** 当前选中的地区，默认成都市·市本级 */
    var currentRegion by mutableStateOf(RegionSelection())
        private set

    /** 更新当前选中的城市和站点 */
    fun update(city: String, station: String) {
        currentRegion = RegionSelection(city = city, station = station)
    }
}

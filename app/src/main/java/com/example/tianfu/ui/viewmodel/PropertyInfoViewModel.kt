package com.example.tianfu.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tianfu.config.RegionStore
import com.example.tianfu.data.PropertyRepository
import com.example.tianfu.network.AuthManager
import com.example.tianfu.network.model.PropertyInfoDto
import kotlinx.coroutines.launch

/**
 * 房产信息查询 ViewModel。
 */
data class PropertyInfoUiState(
    val name: String = "",
    val idNumber: String = "",
    // 行政区域：只传一级城市名给后端
    val city: String = RegionStore.currentRegion.city,
    val properties: List<PropertyInfoDto> = emptyList(),
    val showDetail: Boolean = false,
    val showNoResultDialog: Boolean = false,
    val message: String? = null,
)

class PropertyInfoViewModel(
    private val repository: PropertyRepository,
) : ViewModel() {

    var uiState by mutableStateOf(PropertyInfoUiState())
        private set

    fun loadUserFromAuth() {
        val user = AuthManager.user
        if (user != null) {
            uiState = uiState.copy(
                name = user.realName,
                idNumber = user.idNumber,
            )
        }
    }

    fun onCityChange(newCity: String) {
        uiState = uiState.copy(city = newCity)
    }

    fun query() {
        viewModelScope.launch {
            val name = uiState.name
            val idNumber = uiState.idNumber
            val city = uiState.city

            if (name.isBlank() || idNumber.isBlank()) {
                uiState = uiState.copy(message = "姓名或证件号码为空")
                return@launch
            }

            try {
                val result = repository.queryPropertyInfo(name, idNumber, city)
                if (result.code == 1) {
                    val list = result.data ?: emptyList()
                    if (list.isEmpty()) {
                        uiState = uiState.copy(
                            properties = emptyList(),
                            showDetail = false,
                            showNoResultDialog = true,
                        )
                    } else {
                        uiState = uiState.copy(
                            properties = list,
                            showDetail = true,
                            showNoResultDialog = false,
                        )
                    }
                } else {
                    uiState = uiState.copy(message = result.msg.ifBlank { "查询失败" })
                }
            } catch (e: Exception) {
                uiState = uiState.copy(message = "网络错误：" + (e.message ?: "未知错误"))
            }
        }
    }

    fun consumeMessage() {
        uiState = uiState.copy(message = null)
    }

    fun dismissNoResultDialog() {
        uiState = uiState.copy(showNoResultDialog = false)
    }
}

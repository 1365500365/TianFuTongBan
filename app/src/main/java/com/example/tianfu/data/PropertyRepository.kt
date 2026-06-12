package com.example.tianfu.data

import com.example.tianfu.network.api.ApiService
import com.example.tianfu.network.model.ApiResponse
import com.example.tianfu.network.model.PropertyInfoDto

class PropertyRepository(
    private val apiService: ApiService,
) {

    suspend fun queryPropertyInfo(
        ownerName: String,
        idNumber: String,
        city: String,
    ): ApiResponse<List<PropertyInfoDto>> {
        return apiService.queryPropertyInfo(ownerName, idNumber, city)
    }
}

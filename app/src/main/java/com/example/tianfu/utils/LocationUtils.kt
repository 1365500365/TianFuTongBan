package com.example.tianfu.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Locale

/**
 * 位置信息获取工具类
 */
object LocationUtils {

    /**
     * 获取当前城市名称
     * 注意：调用前请确保已动态申请 ACCESS_COARSE_LOCATION 或 ACCESS_FINE_LOCATION 权限。
     * * @param context 上下文
     * @param defaultCity
     * @return 城市名称字符串
     */
    @SuppressLint("MissingPermission")
    suspend fun getCityName(context: Context, defaultCity: String = "定位中"): String {
        return withContext(Dispatchers.IO) {
            try {
                // 1. 检查权限
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Timber.w("Location permission not granted, using default city.")
                    return@withContext defaultCity
                }

                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                // 2. 尝试获取最后已知位置
                val providers = locationManager.getProviders(true)
                var bestLocation: Location? = null
                for (provider in providers) {
                    val location = locationManager.getLastKnownLocation(provider) ?: continue
                    if (bestLocation == null || location.accuracy < bestLocation.accuracy) {
                        bestLocation = location
                    }
                }

                if (bestLocation == null) {
                    return@withContext defaultCity
                }

                // 3. 逆地理编码获取城市名
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(bestLocation.latitude, bestLocation.longitude, 1)

                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val city = address.locality ?: address.subAdminArea ?: defaultCity
                    return@withContext city
                }

                defaultCity
            } catch (e: Exception) {
                Timber.e(e, "Failed to get city name")
                defaultCity
            }
        }
    }
}
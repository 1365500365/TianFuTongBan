package com.example.tianfu.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tianfu.config.RegionData
import com.example.tianfu.config.RegionStore
import com.example.tianfu.navigation.currentComposeNavigator
import com.example.tianfu.ui.component.SecondaryPageTemplate

/**
 * 地区选择页面：左侧为城市列表，右侧为站点/区县列表。
 * 数据根据用户提供的截图手工录入，作为示例已经覆盖四川省主要市州及常用区域。
 */
@Composable
fun RegionPickerScreen() {
    val navigator = currentComposeNavigator

    // 所有市州列表与站点数据，统一从 RegionData 读取
    val cities = remember { RegionData.allCities }
    val cityStations = remember { RegionData.cityStations }

    val selectedCityState = remember { mutableStateOf(RegionStore.currentRegion.city) }

    SecondaryPageTemplate(
        title = "站点选择",
        loadingTitle = "正在加载站点",
        needsLoading = false,
        onBackClick = { navigator.navigateUp() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // 左侧城市列表
            CityList(
                cities = cities,
                selectedCity = selectedCityState.value,
                onCitySelected = { selectedCityState.value = it },
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.4f)
            )

            // 右侧站点列表
            val stations = cityStations[selectedCityState.value] ?: listOf("市本级")
            StationList(
                city = selectedCityState.value,
                stations = stations,
                onStationSelected = { station ->
                    RegionStore.update(selectedCityState.value, station)
                    navigator.navigateUp()
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.6f)
            )
        }
    }
}

@Composable
private fun CityList(
    cities: List<String>,
    selectedCity: String,
    onCitySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(cities) { city ->
                val isSelected = city == selectedCity
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .background(if (isSelected) Color.White else Color(0xFFF5F5F5))
                        .clickable { onCitySelected(city) }
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = city,
                        fontSize = 15.sp,
                        color = if (isSelected) Color(0xFF0080FF) else Color(0xFF333333)
                    )
                }
            }
        }
    }
}

@Composable
private fun StationList(
    city: String,
    stations: List<String>,
    onStationSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(Color.White)
    ) {
        // 顶部当前城市标题
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = city, fontSize = 16.sp, color = Color(0xFF333333))
            Text(text = "请选择站点", fontSize = 12.sp, color = Color(0xFF999999))
        }

        Card(
            modifier = Modifier
                .fillMaxSize(),
            shape = RoundedCornerShape(topStart = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(stations) { station ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clickable { onStationSelected(station) }
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(text = station, fontSize = 15.sp, color = Color(0xFF333333))
                    }
                }
            }
        }
    }
}

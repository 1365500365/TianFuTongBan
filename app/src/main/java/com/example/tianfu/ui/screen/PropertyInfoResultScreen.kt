package com.example.tianfu.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tianfu.network.model.PropertyInfoDto

@Composable
fun PropertyInfoResultList(
    properties: List<PropertyInfoDto>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(properties) { item ->
            PropertyDetailSection(item = item)
        }
    }
}

@Composable
fun PropertyDetailSection(item: PropertyInfoDto) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val district: String? = item.district
        val street: String? = item.street
        val doorNo: String? = item.doorNo
        val floor: String? = item.floor

        val title = buildString {
            append(district.orEmpty())
            append(street.orEmpty())
            append(doorNo.orEmpty())
            item.buildingNo?.let { append("${it}幢") }
            item.unitNo?.let { append("${it}单元") }
            if (!floor.isNullOrBlank()) {
                append("${floor}楼")
            }
            item.roomNo?.let { append("${it}号") }
        }

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
        )

        Spacer(modifier = Modifier.height(12.dp))

        DetailRow(label = "所在区", value = district)
        DetailRow(label = "街道", value = street)
        DetailRow(label = "门牌号", value = doorNo)
        DetailRow(label = "栋号", value = item.buildingNo?.toString())
        DetailRow(label = "单元", value = item.unitNo?.toString())
        DetailRow(label = "楼层", value = floor)
        DetailRow(label = "房号", value = item.roomNo?.toString())
        DetailRow(label = "规划用途", value = item.plannedUse)
        DetailRow(label = "套内面积", value = item.areaInCasing?.toString())
        DetailRow(label = "公摊面积", value = item.sharedArea?.toString())
    }
}

@Composable
fun DetailRow(label: String, value: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            modifier = Modifier.width(72.dp),
            color = androidx.compose.ui.graphics.Color.Gray
        )
        Spacer(modifier = Modifier.width(50.dp))

        Text(
            text = value.orEmpty(),
            fontSize = 16.sp,
            modifier = Modifier.weight(1f),
            color = androidx.compose.ui.graphics.Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PropertyInfoResultPreview() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            PropertyInfoResultList(
                properties = listOf(
                    PropertyInfoDto(
                        district = "锦江区",
                        street = "红星路",
                        doorNo = "99号",
                        buildingNo = 1,
                        unitNo = 2,
                        floor = "12",
                        roomNo = 1203,
                        plannedUse = "住宅",
                        areaInCasing = 120.5,
                        sharedArea = 20.3,
                    )
                )
            )
        }
    }
}

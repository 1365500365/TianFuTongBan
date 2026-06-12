package com.example.tianfu.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tianfu.R

@Composable
fun ScreenTopSearchBar(
    modifier: Modifier = Modifier,
    locationText: String = "成都市",
    searchHint: String = "请输入关键词",
    onLocationClick: (() -> Unit)? = null,
    onSearchClick: (() -> Unit)? = null,
    onScanClick: (() -> Unit)? = null,
    onNotificationClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$locationText ▼",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.clickable(enabled = onLocationClick != null) { onLocationClick?.invoke() }
        )

        Spacer(modifier = Modifier.width(32.dp))


        Row(
            modifier = Modifier.weight(1f)
                .height(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.84f))
                .clickable(
                    enabled = onSearchClick != null
                ) { onSearchClick?.invoke() }
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(R.drawable.img_nav_search),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = searchHint,
                color = Color(0xFF8A8A8A),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            painterResource(R.drawable.sys),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(24.dp)
                .clickable(enabled = onScanClick != null) { onScanClick?.invoke() }
        )

        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            painterResource(R.drawable.myxx),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(24.dp)
                .clickable(enabled = onNotificationClick != null) { onNotificationClick?.invoke() }
        )
    }
}

package com.example.tianfu.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tianfu.R

@Composable
fun ScreenTopBackground(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.bg_home_top),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
        )

        Image(
            painter = painterResource(id = R.drawable.logo_tftb_white),
            contentDescription = "天府通办",
            modifier = Modifier
                .height(24.dp)
                .align(Alignment.CenterEnd)
                .offset(x = (-16).dp),
            contentScale = ContentScale.FillHeight,
        )
    }
}
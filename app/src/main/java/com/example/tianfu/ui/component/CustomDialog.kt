package com.example.tianfu.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tianfu.theme.PRIMARY_BLUE

/**
 * 仿原生风格的通用提示弹窗组件
 */
@Composable
fun CustomDialog(
    title: String = "温馨提示",
    content: String,
    cancelText: String = "取消",
    confirmText: String = "确认",
    showCancel: Boolean = true,
    confirmTextColor: Color = PRIMARY_BLUE,
    onCancel: () -> Unit = {},
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // 标题
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 内容
                Text(
                    text = content,
                    fontSize = 15.sp,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    if (showCancel) {
                        // 取消按钮
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                ) { onCancel() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cancelText,
                                fontSize = 16.sp,
                                color = Color(0xFF999999)
                            )
                        }

                        // 垂直分割线
                        VerticalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                    }

                    // 确认按钮
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) { onConfirm() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = confirmText,
                            fontSize = 16.sp,
                            color = confirmTextColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomDialog(
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
    cancelText: String = "取消",
    confirmText: String = "确认",
    showCancel: Boolean = true,
    confirmTextColor: Color = PRIMARY_BLUE,
    onCancel: () -> Unit = {},
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // 标题
                title()

                Spacer(modifier = Modifier.height(16.dp))

                // 内容
                content()

                Spacer(modifier = Modifier.height(24.dp))

                HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    if (showCancel) {
                        // 取消按钮
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                ) { onCancel() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cancelText,
                                fontSize = 16.sp,
                                color = Color(0xFF999999)
                            )
                        }

                        // 垂直分割线
                        VerticalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                    }

                    // 确认按钮
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) { onConfirm() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = confirmText,
                            fontSize = 16.sp,
                            color = confirmTextColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
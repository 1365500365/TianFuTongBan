package com.example.tianfu.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.tianfu.navigation.currentComposeNavigator
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 扫一扫：CameraX 实时预览 + ML Kit 二维码/条形码识别。
 *
 * - 顶部：返回 / 标题「扫一扫」/「相册」。
 * - 中间扫描框 + 提示「将二维码或条形码放入框内，即可自动扫描」。
 * - 首次进入请求系统相机权限；「相册」可从图库选图识别。
 * - 识别到结果后弹框展示，点「确定」继续扫描。
 */
@androidx.annotation.OptIn(ExperimentalGetImage::class)
@OptIn(ExperimentalGetImage::class)
@Composable
fun ScanScreen() {
    val navigator = currentComposeNavigator
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var scannedResult by remember { mutableStateOf<String?>(null) }
    // 识别成功后置位，避免重复弹框；关闭弹框后复位
    val scannedFlag = remember { mutableStateOf(false) }

    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val scanner = remember { BarcodeScanning.getClient() }

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
            scanner.close()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                val image = InputImage.fromFilePath(context, uri)
                scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        val value = barcodes.firstOrNull()?.rawValue
                        if (value != null) {
                            scannedResult = value
                        } else {
                            Toast.makeText(context, "未识别到二维码/条形码", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "图片识别失败", Toast.LENGTH_SHORT).show()
                    }
            } catch (e: Exception) {
                Toast.makeText(context, "图片读取失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // 相机预览
        if (hasPermission) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val providerFuture = ProcessCameraProvider.getInstance(ctx)
                    providerFuture.addListener({
                        val cameraProvider = providerFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val analysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                        analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                            val mediaImage = imageProxy.image
                            if (mediaImage != null && !scannedFlag.value) {
                                val input = InputImage.fromMediaImage(
                                    mediaImage, imageProxy.imageInfo.rotationDegrees
                                )
                                scanner.process(input)
                                    .addOnSuccessListener { barcodes ->
                                        val value = barcodes.firstOrNull()?.rawValue
                                        if (value != null && !scannedFlag.value) {
                                            scannedFlag.value = true
                                            scannedResult = value
                                        }
                                    }
                                    .addOnCompleteListener { imageProxy.close() }
                            } else {
                                imageProxy.close()
                            }
                        }
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            analysis
                        )
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                }
            )
        }

        // 中间扫描框 + 提示
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ScanFrame()
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "将二维码或条形码放入框内，即可自动扫描",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }

        // 顶部栏：返回 / 扫一扫 / 相册
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(48.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "返回",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
                    .size(30.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { navigator.navigateUp() }
            )
            Text(
                text = "扫一扫",
                color = Color.White,
                fontSize = 17.sp,
                modifier = Modifier.align(Alignment.Center)
            )
            Text(
                text = "相册",
                color = Color.White,
                fontSize = 15.sp,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { galleryLauncher.launch("image/*") }
            )
        }

        // 结果弹框
        scannedResult?.let { result ->
            AlertDialog(
                onDismissRequest = {
                    scannedResult = null
                    scannedFlag.value = false
                },
                title = { Text("扫描结果") },
                text = { Text(result) },
                confirmButton = {
                    TextButton(onClick = {
                        scannedResult = null
                        scannedFlag.value = false
                    }) { Text("确定") }
                }
            )
        }
    }
}

/** 中间方形扫描框，四角绘制蓝色折角。 */
@Composable
private fun ScanFrame() {
    Box(modifier = Modifier.size(250.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 4.dp.toPx()
            val corner = 24.dp.toPx()
            val color = Color(0xFF2DA6FF)
            val w = size.width
            val h = size.height
            // 左上
            drawLine(color, androidx.compose.ui.geometry.Offset(0f, 0f), androidx.compose.ui.geometry.Offset(corner, 0f), stroke)
            drawLine(color, androidx.compose.ui.geometry.Offset(0f, 0f), androidx.compose.ui.geometry.Offset(0f, corner), stroke)
            // 右上
            drawLine(color, androidx.compose.ui.geometry.Offset(w - corner, 0f), androidx.compose.ui.geometry.Offset(w, 0f), stroke)
            drawLine(color, androidx.compose.ui.geometry.Offset(w, 0f), androidx.compose.ui.geometry.Offset(w, corner), stroke)
            // 左下
            drawLine(color, androidx.compose.ui.geometry.Offset(0f, h - corner), androidx.compose.ui.geometry.Offset(0f, h), stroke)
            drawLine(color, androidx.compose.ui.geometry.Offset(0f, h), androidx.compose.ui.geometry.Offset(corner, h), stroke)
            // 右下
            drawLine(color, androidx.compose.ui.geometry.Offset(w - corner, h), androidx.compose.ui.geometry.Offset(w, h), stroke)
            drawLine(color, androidx.compose.ui.geometry.Offset(w, h - corner), androidx.compose.ui.geometry.Offset(w, h), stroke)
        }
    }
}

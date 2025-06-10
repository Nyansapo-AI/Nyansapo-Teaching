package com.nyansapoai.teaching.presentation.common.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import com.nyansapoai.teaching.presentation.common.permissions.RequestAppPermissions
import com.nyansapoai.teaching.utils.Utils.saveToImageFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CapturableComposable(
    modifier: Modifier = Modifier,
    onCaptured: (ImageBitmap) -> Unit = {},
    onCapturedByteArray : (ByteArray) -> Unit = {},
    content: @Composable () -> Unit,
    shouldCapture: Boolean = false
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    // Request permissions to write and read external storage
    var permissionGranted by remember { mutableStateOf(false) }

    RequestAppPermissions(
        permissionsArray = arrayOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
        ),
        onSuccess = {
            permissionGranted = true
        },

        )

    if (permissionGranted){
        Box(
            modifier = modifier
                .drawWithCache {
                    onDrawWithContent {

                        graphicsLayer.record {
                            this@onDrawWithContent.drawContent()
                        }
                        drawLayer(graphicsLayer = graphicsLayer)

                        if (shouldCapture){
                            coroutineScope.launch(Dispatchers.IO) {
                                val bitmap = graphicsLayer.toImageBitmap()

                                onCaptured(bitmap)

                                val file = bitmap.saveToImageFile(
                                    context = context,
                                    filename = null
                                )

                                file?.let { file ->
                                    Log.d("CapturedImage", "Image saved to: ${file.absolutePath}")
                                    onCapturedByteArray(file.readBytes())
                                }
                            }

                        }
                    }
                }
        ) {
            content()
        }

    }


}








package com.nyansapoai.teaching.presentation.common.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import com.nyansapoai.teaching.utils.Utils.saveToImageFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ScreenshotComposable(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    onFilePathChange: (String) -> Unit = {},
    fileName: String? = null,
    shouldCapture: Boolean = false
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    Box(
        modifier = modifier
            .drawWithCache {
                onDrawWithContent {

                    graphicsLayer.record {
                        this@onDrawWithContent.drawContent()
                    }
                    drawLayer(graphicsLayer = graphicsLayer)

                    if (shouldCapture) {
                        coroutineScope.launch(Dispatchers.IO) {
                            val bitmap = graphicsLayer.toImageBitmap()

                            val file = bitmap.saveToImageFile(
                                context = context,
                                filename = fileName
                            )

                            file?.let { file ->
                                Log.d("CapturedImage", "Image saved to: ${file.absolutePath}")
                                onFilePathChange(file.absolutePath)
                            }
                        }
                    }

                }
            }
    ) {
        content()
    }

}








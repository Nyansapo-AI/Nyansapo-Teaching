package com.nyansapoai.teaching.presentation.common.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.common.permissions.RequestAppPermissions
import com.nyansapoai.teaching.utils.Utils
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


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppTakeScreenShoot(
    modifier: Modifier = Modifier,
//    content: @Composable () -> Unit
) {
    var shouldCapture by remember { mutableStateOf(false) }
    var capturedContent by remember { mutableStateOf<ImageBitmap?>(null) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {

        capturedContent?.let{ content ->
            Image(
                bitmap = content,
                contentDescription = "Captured Content",
                modifier = Modifier
                    .padding(16.dp)
                    .size(200.dp)
            )
        }


        CapturableComposable(
            modifier = modifier,
            onCaptured = { bitmap ->
                capturedContent = bitmap
            },
            shouldCapture = shouldCapture,
            content = {
                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .background(MaterialTheme.colorScheme.error)
                ) {
                    Column {
                        Image(
                            painter = painterResource(R.drawable.eraser),
                            contentDescription = "Eraser Icon",
                        )

                        Text(
                            text = "Capture this content",
                            modifier = Modifier.padding(16.dp),
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        )


        Button(
            onClick = {
                shouldCapture = true // Set to true to trigger capture
            },
            modifier = Modifier
//                .padding(16.dp)

        ) {
            Text(text = "Capture Screen")
        }
    }


    // Trigger capture when needed, e.g., on a button click
    // This is just an example, you can replace it with your own logic
    if (shouldCapture) {
        shouldCapture = false // Reset after capturing
    }
}





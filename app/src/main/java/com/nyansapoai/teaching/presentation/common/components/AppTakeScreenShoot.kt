package com.nyansapoai.teaching.presentation.common.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.R
import kotlinx.coroutines.launch

@Composable
fun CapturableComposable(
    modifier: Modifier = Modifier,
    onCaptured: (ImageBitmap) -> Unit = {},
    content: @Composable () -> Unit,
    shouldCapture: Boolean = false
) {
    val density = LocalDensity.current
//    var shouldCapture by remember { mutableStateOf(false) }

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
                        coroutineScope.launch {
                            val bitmap = graphicsLayer.toImageBitmap()


                            onCaptured(bitmap)
                        }
                    }
                }
            }
    ) {
        content()
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





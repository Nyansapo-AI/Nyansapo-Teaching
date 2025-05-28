package com.nyansapoai.teaching.presentation.common.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AppTouchInput(modifier: Modifier = Modifier) {

    val path = remember { Path() }
    val touchPoints = remember { mutableListOf<Offset>() }

    /*
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        touchPoints.clear()
                        path.moveTo(offset.x, offset.y)
                        touchPoints.add(offset)
                    },
                    onDrag = { change, _ ->
                        val offset = change.position
                        path.lineTo(offset.x, offset.y)
                        touchPoints.add(offset)
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawPath(
                path = path,
                color = Color.Black,
                style = Stroke(width = 4f)
            )
        }
    }

     */


    /* MultiTouchDrawingCanvas() */

    PrecisionEraserCanvas()
}


@Composable
fun MultiTouchDrawingCanvas() {
    var paths by remember { mutableStateOf(listOf<PressurePath>()) }
    var activePaths by remember { mutableStateOf(mapOf<Int, PressurePath>()) }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                awaitEachGesture {
                    do {
                        val event = awaitPointerEvent()

                        event.changes.forEach { change ->
                            val pointerId = change.id.value.toInt()

                            when (change.type) {
                                PointerType.Touch -> {
                                    if (change.pressed) {
                                        if (pointerId !in activePaths) {
                                            // Start new path
                                            val pressure = change.pressure
                                            val strokeWidth = (pressure * 20f).coerceIn(2f, 15f)

                                            activePaths = activePaths + (pointerId to PressurePath(
                                                points = listOf(
                                                    PressurePoint(
                                                        offset = change.position,
                                                        pressure = pressure
                                                    )
                                                ),
                                                color = getRandomColor(),
                                                baseStrokeWidth = strokeWidth
                                            ))
                                        } else {
                                            // Continue existing path
                                            val existingPath = activePaths[pointerId]
                                            if (existingPath != null) {
                                                val newPoint = PressurePoint(
                                                    offset = change.position,
                                                    pressure = change.pressure
                                                )

                                                activePaths = activePaths + (pointerId to existingPath.copy(
                                                    points = existingPath.points + newPoint
                                                ))
                                            }
                                        }
                                        change.consume()
                                    } else {
                                        // Finger lifted - move to completed paths
                                        activePaths[pointerId]?.let { completedPath ->
                                            paths = paths + completedPath
                                            activePaths = activePaths - pointerId
                                        }
                                    }
                                }
                            }
                        }
                    } while (event.changes.any { it.pressed })
                }
            }
    ) {

        // Draw completed paths
        paths.forEach { pressurePath ->
            drawPressurePath(pressurePath)
        }

        // Draw active paths
        activePaths.values.forEach { pressurePath ->
            drawPressurePath(pressurePath)
        }
    }
}

fun DrawScope.drawPressurePath(pressurePath: PressurePath) {
    if (pressurePath.points.size < 2) return

    for (i in 0 until pressurePath.points.size - 1) {
        val startPoint = pressurePath.points[i]
        val endPoint = pressurePath.points[i + 1]

        // Vary stroke width based on pressure
        val startWidth = pressurePath.baseStrokeWidth * startPoint.pressure
        val endWidth = pressurePath.baseStrokeWidth * endPoint.pressure
        val avgWidth = (startWidth + endWidth) / 2

        drawLine(
            color = pressurePath.color,
            start = startPoint.offset,
            end = endPoint.offset,
            strokeWidth = avgWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun GestureDrawingCanvas() {
    var shapes by remember { mutableStateOf(listOf<DrawnShape>()) }
    var currentGesture by remember { mutableStateOf<String?>(null) }

    Column {
        Text(
            text = currentGesture ?: "Draw with gestures",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineSmall
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .pointerInput(Unit) {
                    detectTransformGestures(
                        onGesture = { _, pan, zoom, rotation ->
                            currentGesture = when {
                                zoom > 1.1f -> "Pinch to zoom out"
                                zoom < 0.9f -> "Pinch to zoom in"
                                rotation > 0.1f -> "Rotating clockwise"
                                rotation < -0.1f -> "Rotating counter-clockwise"
                                pan.getDistance() > 10f -> "Panning"
                                else -> null
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            shapes = shapes + DrawnShape.Circle(
                                center = offset,
                                radius = 30f,
                                color = getRandomColor()
                            )
                            currentGesture = "Tap to add circle"
                        },
                        onDoubleTap = { offset ->
                            shapes = shapes + DrawnShape.Square(
                                center = offset,
                                size = 60f,
                                color = getRandomColor()
                            )
                            currentGesture = "Double tap to add square"
                        },
                        onLongPress = { offset ->
                            // Remove shapes near long press
                            shapes = shapes.filterNot { shape ->
                                when (shape) {
                                    is DrawnShape.Circle ->
                                        (shape.center - offset).getDistance() < shape.radius + 20f
                                    is DrawnShape.Square ->
                                        offset.x in (shape.center.x - shape.size/2)..(shape.center.x + shape.size/2) &&
                                                offset.y in (shape.center.y - shape.size/2)..(shape.center.y + shape.size/2)
                                }
                            }
                            currentGesture = "Long press to remove shapes"
                        }
                    )
                }
        ) {
            shapes.forEach { shape ->
                when (shape) {
                    is DrawnShape.Circle -> {
                        drawCircle(
                            color = shape.color,
                            radius = shape.radius,
                            center = shape.center
                        )
                    }
                    is DrawnShape.Square -> {
                        drawRect(
                            color = shape.color,
                            topLeft = Offset(
                                shape.center.x - shape.size / 2,
                                shape.center.y - shape.size / 2
                            ),
                            size = Size(shape.size, shape.size)
                        )
                    }
                }
            }
        }
    }
}

data class PressurePoint(
    val offset: Offset,
    val pressure: Float
)

data class PressurePath(
    val points: List<PressurePoint>,
    val color: Color,
    val baseStrokeWidth: Float
)

sealed class DrawnShape {
    data class Circle(
        val center: Offset,
        val radius: Float,
        val color: Color
    ) : DrawnShape()

    data class Square(
        val center: Offset,
        val size: Float,
        val color: Color
    ) : DrawnShape()
}

fun getRandomColor(): Color {
    val colors = listOf(
        Color.Red, Color.Blue, Color.Green, Color.Yellow,
        Color.Magenta, Color.Cyan, Color.Black, Color.Gray
    )
    return colors.random()
}


@Composable
fun PrecisionEraserCanvas() {
    var drawingLayers by remember { mutableStateOf(listOf<DrawingLayer>()) }
    var currentLayer by remember { mutableStateOf<DrawingLayer?>(null) }
    var isEraserMode by remember { mutableStateOf(false) }
    var eraserSize by remember { mutableStateOf(20.dp) }
    var showEraserPreview by remember { mutableStateOf(false) }
    var eraserPreviewPosition by remember { mutableStateOf(Offset.Zero) }

    Box {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            if (isEraserMode) {
                                showEraserPreview = true
                                eraserPreviewPosition = offset
                                // Create eraser stroke
                                currentLayer = DrawingLayer(
                                    paths = listOf(
                                        EraserPath(
                                            path = Path().apply {
                                                moveTo(offset.x, offset.y)
                                                addOval(androidx.compose.ui.geometry.Rect(
                                                    offset.x - eraserSize.toPx() / 2,
                                                    offset.y - eraserSize.toPx() / 2,
                                                    offset.x + eraserSize.toPx() / 2,
                                                    offset.y + eraserSize.toPx() / 2
                                                ))
                                            },
                                            size = eraserSize.toPx()
                                        )
                                    ),
                                    isEraserLayer = true
                                )
                            } else {
                                currentLayer = DrawingLayer(
                                    paths = listOf(
                                        DrawPath(
                                            path = Path().apply { moveTo(offset.x, offset.y) },
                                            color = Color.Black,
                                            strokeWidth = 8.dp.toPx()
                                        )
                                    ),
                                    isEraserLayer = false
                                )
                            }
                        },
                        onDragEnd = {
                            showEraserPreview = false
                            currentLayer?.let { layer ->
                                drawingLayers = drawingLayers + layer
                                currentLayer = null
                            }
                        },
                        onDrag = { change, _ ->
                            if (isEraserMode) {
                                eraserPreviewPosition = change.position
                                currentLayer?.let { layer ->
                                    val newEraserPath = EraserPath(
                                        path = Path().apply {
                                            moveTo(change.position.x, change.position.y)
                                            addOval(androidx.compose.ui.geometry.Rect(
                                                change.position.x - eraserSize.toPx() / 2,
                                                change.position.y - eraserSize.toPx() / 2,
                                                change.position.x + eraserSize.toPx() / 2,
                                                change.position.y + eraserSize.toPx() / 2
                                            ))
                                        },
                                        size = eraserSize.toPx()
                                    )
                                    currentLayer = layer.copy(
                                        paths = layer.paths + newEraserPath
                                    )
                                }
                            } else {
                                currentLayer?.let { layer ->
                                    val drawPath = layer.paths.first() as DrawPath
                                    drawPath.path.lineTo(change.position.x, change.position.y)
                                }
                            }
                        }
                    )
                }
        ) {
            // Draw all layers with proper blending
            drawIntoCanvas { canvas ->
                with(canvas.nativeCanvas) {
                    val checkPoint = saveLayer(null, null)

                    // Draw all completed layers
                    drawingLayers.forEach { layer ->
                        layer.paths.forEach { pathData ->
                            when (pathData) {
                                is DrawPath -> {
                                    drawPath(
                                        path = pathData.path,
                                        color = pathData.color,
                                        style = Stroke(
                                            width = pathData.strokeWidth,
                                            cap = StrokeCap.Round,
                                            join = StrokeJoin.Round
                                        )
                                    )
                                }
                                is EraserPath -> {
                                    drawPath(
                                        path = pathData.path,
                                        color = Color.Transparent,
                                        blendMode = BlendMode.Clear
                                    )
                                }
                            }
                        }
                    }

                    // Draw current layer
                    currentLayer?.let { layer ->
                        layer.paths.forEach { pathData ->
                            when (pathData) {
                                is DrawPath -> {
                                    drawPath(
                                        path = pathData.path,
                                        color = pathData.color,
                                        style = Stroke(
                                            width = pathData.strokeWidth,
                                            cap = StrokeCap.Round,
                                            join = StrokeJoin.Round
                                        )
                                    )
                                }
                                is EraserPath -> {
                                    drawPath(
                                        path = pathData.path,
                                        color = Color.Transparent,
                                        blendMode = BlendMode.Clear
                                    )
                                }
                            }
                        }
                    }

                    restoreToCount(checkPoint)
                }
            }
        }

        // Eraser preview overlay
        if (showEraserPreview && isEraserMode) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                drawCircle(
                    color = Color.Gray.copy(alpha = 0.5f),
                    radius = eraserSize.toPx() / 2,
                    center = eraserPreviewPosition,
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }
    }
}


// Data classes for advanced eraser
sealed class PathData

data class DrawPath(
    val path: Path,
    val color: Color,
    val strokeWidth: Float
) : PathData()

data class EraserPath(
    val path: Path,
    val size: Float
) : PathData()

data class DrawingLayer(
    val paths: List<PathData>,
    val isEraserLayer: Boolean
)


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AppTouchInputPreview() {
    AppTouchInput(
        modifier = Modifier.fillMaxSize()
    )
}
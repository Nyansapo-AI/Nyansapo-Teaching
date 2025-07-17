package com.nyansapoai.teaching.presentation.common.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
fun AppTouchInput(
    modifier: Modifier = Modifier,
    isEraserMode: Boolean = false,
    brushColor: Color = Color.Black,

    ) {

    MultiTouchDrawingCanvas(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary),
        isEraserMode = isEraserMode,
        eraserSize = 60f,
        brushColor = brushColor
    )
}


@Composable
fun MultiTouchDrawingCanvas(
    modifier: Modifier = Modifier,
    isEraserMode: Boolean = false,
    eraserSize: Float = 30f,
    brushColor: Color = Color.Black,
) {
    var paths by remember { mutableStateOf(listOf<PressurePath>()) }
    var activePaths by remember { mutableStateOf(mapOf<Int, PressurePath>()) }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary)
            .pointerInput(isEraserMode) {
                awaitEachGesture {
                    do {
                        val event = awaitPointerEvent()

                        event.changes.forEach { change ->
                            val pointerId = change.id.value.toInt()

                            when (change.type) {
                                PointerType.Touch -> {
                                    if (change.pressed) {
                                        if (pointerId !in activePaths) {
                                            if (isEraserMode) {
                                                // In eraser mode, remove points near touch
                                                eraseAtPosition(change.position, eraserSize, paths) { newPaths ->
                                                    paths = newPaths
                                                }

                                                // Add an eraser path for visual feedback
                                                activePaths = activePaths + (pointerId to PressurePath(
                                                    points = listOf(PressurePoint(change.position, 1f)),
                                                    color = Color.LightGray.copy(alpha = 0.5f),
                                                    baseStrokeWidth = eraserSize,
                                                    isEraser = true
                                                ))
                                            } else {
                                                // Start new drawing path
                                                val pressure = change.pressure
                                                val strokeWidth = (pressure * 20f).coerceIn(10f, 15f)

                                                activePaths = activePaths + (pointerId to PressurePath(
                                                    points = listOf(
                                                        PressurePoint(
                                                            offset = change.position,
                                                            pressure = pressure
                                                        )
                                                    ),
                                                    color = brushColor,
                                                    baseStrokeWidth = strokeWidth,
                                                    isEraser = false
                                                ))
                                            }
                                        } else {
                                            // Continue existing path
                                            val existingPath = activePaths[pointerId]
                                            if (existingPath != null) {
                                                val newPoint = PressurePoint(
                                                    offset = change.position,
                                                    pressure = change.pressure
                                                )

                                                if (isEraserMode) {
                                                    // Continue erasing
                                                    eraseAtPosition(change.position, eraserSize, paths) { newPaths ->
                                                        paths = newPaths
                                                    }
                                                }

                                                activePaths = activePaths + (pointerId to existingPath.copy(
                                                    points = existingPath.points + newPoint
                                                ))
                                            }
                                        }
                                        change.consume()
                                    } else {
                                        // Finger lifted
                                        activePaths[pointerId]?.let { completedPath ->
                                            if (!completedPath.isEraser) {
                                                // Only add to paths if not an eraser
                                                paths = paths + completedPath
                                            }
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
            if (pressurePath.isEraser) {
                // Draw eraser indicator
                drawCircle(
                    color = pressurePath.color,
                    radius = pressurePath.baseStrokeWidth / 2,
                    center = pressurePath.points.last().offset,
                    style = Stroke(width = 2f)
                )
            } else {
                drawPressurePath(pressurePath)
            }
        }
    }
}

// Helper function to erase strokes that intersect with eraser
private fun eraseAtPosition(
    position: Offset,
    eraserSize: Float,
    paths: List<PressurePath>,
    onPathsChanged: (List<PressurePath>) -> Unit
) {
    val eraserRadius = eraserSize / 2
    val eraserRadiusSq = eraserRadius * eraserRadius

    val newPaths = mutableListOf<PressurePath>()

    paths.forEach { path ->
        val points = path.points

        // Skip this path if it's completely erased
        if (points.all { point ->
                val dx = position.x - point.offset.x
                val dy = position.y - point.offset.y
                dx * dx + dy * dy <= eraserRadiusSq
            }) {
            return@forEach
        }

        // Check if any point is within eraser radius
        val hasPointsToErase = points.any { point ->
            val dx = position.x - point.offset.x
            val dy = position.y - point.offset.y
            dx * dx + dy * dy <= eraserRadiusSq
        }

        if (!hasPointsToErase) {
            // Keep the path unchanged
            newPaths.add(path)
        } else {
            // Split path into segments that aren't erased
            val segments = mutableListOf<List<PressurePoint>>()
            var currentSegment = mutableListOf<PressurePoint>()

            points.forEach { point ->
                val dx = position.x - point.offset.x
                val dy = position.y - point.offset.y
                val distanceSq = dx * dx + dy * dy

                if (distanceSq > eraserRadiusSq) {
                    // Point is outside eraser radius
                    currentSegment.add(point)
                } else {
                    // Point is within eraser radius
                    if (currentSegment.isNotEmpty()) {
                        segments.add(currentSegment.toList())
                        currentSegment = mutableListOf()
                    }
                }
            }

            if (currentSegment.isNotEmpty()) {
                segments.add(currentSegment)
            }

            // Add non-empty segments as new paths
            segments.filter { it.size > 1 }.forEach { segment ->
                newPaths.add(path.copy(points = segment))
            }
        }
    }

    onPathsChanged(newPaths)
}

// Updated PressurePath to include isEraser flag
data class PressurePath(
    val points: List<PressurePoint>,
    val color: Color,
    val baseStrokeWidth: Float,
    val isEraser: Boolean = false
)


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

/*
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
*/

data class PressurePoint(
    val offset: Offset,
    val pressure: Float
)

/*
data class PressurePath(
    val points: List<PressurePoint>,
    val color: Color,
    val baseStrokeWidth: Float
)

 */

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





@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AppTouchInputPreview() {
    AppTouchInput(
        modifier = Modifier.fillMaxSize()
    )
}
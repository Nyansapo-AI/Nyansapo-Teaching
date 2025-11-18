package com.nyansapoai.teaching.presentation.schools.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.presentation.schools.LearningLevelDescription
import com.nyansapoai.teaching.presentation.schools.Level
import com.nyansapoai.teaching.ui.theme.lightPrimary

@Composable
fun LearningLevelItem(
    modifier: Modifier = Modifier,
    levelDescription: LearningLevelDescription
) {
    var containerWidth by remember { mutableIntStateOf(0) }

    ElevatedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = lightPrimary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        ),
        modifier = modifier
            .onSizeChanged { size ->
                containerWidth = size.width
            }
            .heightIn(min = 200.dp)
            .widthIn(max = 400.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = levelDescription.type,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Box(modifier = Modifier.fillMaxWidth()) {

                Layout(
                    content = {
                        levelDescription.data.forEach { level ->
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .background(level.color)
                                    .height(28.dp)
                            ) {
                                Text(
                                    text = level.value.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = contentColorFor(level.color),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { measurables, constraints ->

                    val totalStudents = levelDescription.totalStudents.coerceAtLeast(0)
                    val totalWidth = constraints.maxWidth

                    // Calculate width for each bar based on its value
                    val placeables = measurables.mapIndexed { index, measurable ->
                        val level = levelDescription.data[index]
                        val barWidth = ((level.value.toFloat() * totalWidth) / totalStudents).toInt().coerceAtLeast(1)

                        // Measure with exact width constraint
                        measurable.measure(
                            constraints.copy(
                                minWidth = barWidth,
                                maxWidth = barWidth
                            )
                        )
                    }

                    // Layout has height of tallest bar
                    val height = placeables.maxOfOrNull { it.height } ?: 0

                    layout(totalWidth, height) {
                        var xPosition = 0

                        placeables.forEach { placeable ->
                            placeable.placeRelative(xPosition, 0)
                            xPosition += placeable.width
                        }
                    }
                }
            }


            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                maxItemsInEachRow = 2,
                modifier = Modifier.fillMaxWidth()
            ) {
                levelDescription.data.forEach { level ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .widthIn(max = (containerWidth / 2 - 16).dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(15.dp)
                                .clip(RectangleShape)
                                .background(level.color)
                        )

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = level.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 1,
                                overflow = TextOverflow.MiddleEllipsis
                            )

                            Text(
                                text = level.value.toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ItemUI(){
    LearningLevelItem(
        levelDescription = LearningLevelDescription(
            type = "Literacy",
            totalStudents = 15,
            data = listOf(
                Level("Letters", 1, Color.Blue),
                Level("Words", 2, Color.Cyan),
                Level("Sentence", 3, Color.Green),
                Level("Paragraph", 4, Color.Yellow),
                Level("Story", 5, Color.Magenta),
            )
        )
    )
}



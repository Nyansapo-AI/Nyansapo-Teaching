package com.nyansapoai.teaching.presentation.camps

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class CampState(
    val greeting: String = "Good Morning",
    val camp: Camp = Camp()
)

data class Camp(
    val id: String = "",
    val name: String = "",
    val learning_level_description: List<LearningLevelDescription> = listOf(
        LearningLevelDescription(
            type = "Numeracy",
            totalStudents = 20,
            data = listOf(
                Level("Count and Match", 1),
                Level("Addition", 2),
                Level("Subtraction", 3),
                Level("Multiplication", 4),
                Level("Division", 5),
                Level("Word Problem", 5),
            )
        ),
        LearningLevelDescription(
            type = "Literacy",
            totalStudents = 20,
            data = listOf(
                Level("Letters", 6, Color.Blue),
                Level("Words", 2, Color.Cyan),
                Level("Sentence", 3, Color.Green),
                Level("Paragraph", 4, Color.Yellow),
                Level("Story", 5, Color.Magenta),
            )
        ),
    ),
)


data class LearningLevelDescription(
    val type: String = "",
    val totalStudents: Int = 0,
    val data: List<Level> =  emptyList()
)

data class Level(
    val name: String = "",
    val value: Int = 0,
    val color: Color = Color.Red
)
package com.nyansapoai.teaching.presentation.camps

import androidx.compose.ui.graphics.Color
import com.nyansapoai.teaching.domain.models.user.NyansapoUser

data class SchoolState(
    val greeting: String = "Good Morning",
    val camp: Camp = Camp(),
    val user: NyansapoUser? = null,
    val showSchoolSelector: Boolean = false

)

data class Camp(
    val id: String = "",
    val name: String = "Camp One",
    val error: String? = null,
    val learning_level_description: List<LearningLevelDescription> = listOf(
        LearningLevelDescription(
            type = "Numeracy",
            totalStudents = 20,
            data = listOf(
                Level("Count and Match", 1, Color.Blue),
                Level("Addition", 2, Color.Cyan),
                Level("Subtraction", 3, Color.Green),
                Level("Multiplication", 4, Color.Yellow),
                Level("Division", 5, Color.Magenta),
                Level("Word Problem", 5, Color.Red),
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
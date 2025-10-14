package com.nyansapoai.teaching.presentation.assessments.literacy.result.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.domain.models.assessments.literacy.MultipleChoicesResult

@Composable
fun ComprehensionQuestion(
    modifier: Modifier = Modifier,
    result: MultipleChoicesResult
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = "Q: ${result.question}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
//            color = Color.White
        )

        result.options.forEach { option ->
            Text(
                text = option,
                style = MaterialTheme.typography.titleLarge,
                color = when{
                    option == result.student_answer && result.passed -> Color(0xFF008000) // Green for correct answer
                    option == result.student_answer && !result.passed -> Color(0xFFFF0000) // Red for wrong selected answer
                    else -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)// Default text color
                }
            )
        }



    }
}
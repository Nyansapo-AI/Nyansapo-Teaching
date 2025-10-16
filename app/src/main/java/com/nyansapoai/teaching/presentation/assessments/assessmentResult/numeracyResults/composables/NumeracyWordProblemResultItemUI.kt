package com.nyansapoai.teaching.presentation.assessments.assessmentResult.numeracyResults.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.domain.models.assessments.numeracy.WordProblemResult

@Composable
fun NumeracyWordProblemResultItemUI(
    modifier: Modifier = Modifier,
    result : WordProblemResult,
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .clickable(
                onClick = onClick
            )
            .padding(4.dp)
    ) {
        Text(
            text = result.question ?: "undefined",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Normal
        )

        Text(
            text = result.metadata.student_answer ?: "undefined",
            style = MaterialTheme.typography.headlineSmall,
            color = if (result.metadata.passed == true) Color(0xFF008000) else Color(0xFFFF0000)
        )
    }
}
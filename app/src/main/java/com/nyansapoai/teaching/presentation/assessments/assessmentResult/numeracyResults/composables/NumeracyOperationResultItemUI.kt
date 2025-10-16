package com.nyansapoai.teaching.presentation.assessments.assessmentResult.numeracyResults.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyOperationResult

@Composable
fun NumeracyOperationResultItemUI(
    modifier: Modifier = Modifier,
    result: NumeracyOperationResult,
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.End,
        modifier = modifier
            .clickable(
                onClick = onClick
            )
            .padding(4.dp)

    ) {
        Text(
            text = result.operations_number1 ?: "undefined",
            style = MaterialTheme.typography.headlineSmall
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getOperationSymbol(result.type),
                style = MaterialTheme.typography.headlineLarge
            )

            Text(
                text = result.operations_number2 ?: "undefined",
                style = MaterialTheme.typography.headlineSmall
            )
        }
        HorizontalDivider(
            thickness = 3.dp,
            modifier = Modifier
                .widthIn(max = 50.dp)
        )
        Text(
            text = result.student_answer ?: "undefined",
            style = MaterialTheme.typography.headlineSmall,
            color = if (result.metadata.passed == true) Color(0xFF008000) else Color(0xFFFF0000)
        )
    }
}


fun getOperationSymbol(type: String): String {
    return when (type) {
        "addition" -> "+"
        "subtraction" -> "-"
        "multiplication" -> "×"
        "division" -> "÷"
        else -> "?"
    }
}
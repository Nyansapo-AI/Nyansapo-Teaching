package com.nyansapoai.teaching.presentation.assessments.numeracy.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun VerticalOperationItem(
    operationSymbol: String,
    firstNumber: Int,
    secondNumber: Int,
){
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = operationSymbol,
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold
        )

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "$firstNumber",
                style = MaterialTheme.typography.displayLarge,
                letterSpacing = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$secondNumber",
                style = MaterialTheme.typography.displayLarge,
                letterSpacing = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun HorizontalOperationItem(
    operationSymbol: String,
    firstNumber: Int,
    secondNumber: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "$firstNumber",
            style = MaterialTheme.typography.displayLarge,
            letterSpacing = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = operationSymbol,
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "$secondNumber",
            style = MaterialTheme.typography.displayLarge,
            letterSpacing = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "=",
            style = MaterialTheme.typography.displayLarge,
            letterSpacing = 12.sp,
            fontWeight = FontWeight.Bold
        )

    }
}


enum class OperationType(
    val title: String,
    val symbol: String
){
    ADDITION(
        title = "Addition",
        symbol = "+"
    ),
    SUBTRACTION(
        title = "Subtraction",
        symbol = "-"
    ),
    MULTIPLICATION(
        title = "Multiplication",
        symbol = "X"
    ),
    DIVISION(
        title = "Division",
        symbol = "÷"
    )
}

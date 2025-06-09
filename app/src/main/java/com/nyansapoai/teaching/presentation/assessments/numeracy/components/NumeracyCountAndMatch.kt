package com.nyansapoai.teaching.presentation.assessments.numeracy.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.common.components.AppButton

@Composable
fun NumeracyCountAndMatch(
    modifier: Modifier = Modifier,
    count: Int = 10,
    onSelectCount: (Int) -> Unit = { /* Handle count selection */ },
    onSubmit: () -> Unit = { /* Handle submission */ }
) {
//        val options = listOf(2, 6, 4, 5)
    val options by remember { mutableStateOf(generateOptionsWithCorrectAnswer(correctNumber = count)) } // Randomly select 4 unique numbers from 1 to 10

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Count and Match",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )

        FlowRow(
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            itemVerticalAlignment = Alignment.CenterVertically,
            maxItemsInEachRow = 5
        ) {
            repeat(count) { index ->
                IconButton(
                    onClick = {
                    },
                    modifier = Modifier
                        .padding(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.football), // You must add this drawable
                        contentDescription = "Football",
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        Divider(
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Grid-like answer options
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OptionButton(
                    number = options[0],
                    onClick = { onSelectCount(options[0]) }
                    )
                OptionButton(
                    number = options[1],
                    onClick = { onSelectCount(options[1]) }
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OptionButton(
                    number = options[2],
                    onClick = { onSelectCount(options[2]) }
                )
                OptionButton(
                    number = options[3],
                    onClick = { onSelectCount(options[3]) }
                )
            }
        }

        AppButton(
            onClick = onSubmit,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
        ) {
            Text("Next", color = Color.Black, fontWeight = FontWeight.Bold)
        }

    }



}

@Composable
fun OptionButton(
    number: Int,
    onClick: () -> Unit,
    isSelected: Boolean = false
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF102C57)),
        modifier = Modifier.size(100.dp)
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.headlineSmall,
            color = if (isSelected) MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f) else Color(0xFFFFC107),
            fontWeight = FontWeight.Bold
        )
    }
}


/**
 * Generates a list of 4 unique numbers that includes the specified number.
 * The remaining 3 numbers are randomly selected from a specified range.
 *
 * @param correctNumber The number that must be included in the result
 * @param range The range to select other numbers from (default: 1..10)
 * @return List of 4 unique integers including the correctNumber
 */
fun generateOptionsWithCorrectAnswer(correctNumber: Int, range: IntRange = 1..10): List<Int> {
    // Create a list without the correct number
    val numbersWithoutCorrect = range.filter { it != correctNumber }.shuffled()

    // Take 3 random numbers
    val randomNumbers = numbersWithoutCorrect.take(3)

    // Create the final list with the correct number and sort it
    return (randomNumbers + correctNumber).shuffled()
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NumeracyCountAndMatchPreview() {
    NumeracyCountAndMatch(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
}
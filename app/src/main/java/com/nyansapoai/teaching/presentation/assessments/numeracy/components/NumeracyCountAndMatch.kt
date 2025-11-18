package com.nyansapoai.teaching.presentation.assessments.numeracy.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.common.components.AppButton
import com.nyansapoai.teaching.presentation.common.components.AppLinearProgressIndicator
import com.nyansapoai.teaching.presentation.common.components.AppShowInstructions

@Composable
fun NumeracyCountAndMatch(
    modifier: Modifier = Modifier,
//    count: Int = 10,
    countList: List<Int> = listOf(1, 2, 6, 7),
    currentIndex: Int = 0,
    onSelectCount: (Int) -> Unit = { /* Handle count selection */ },
    onSubmit: () -> Unit = { /* Handle submission */ },
    selectedCount: Int? = null, // Optional selected count for highlighting
) {

    if (countList.isEmpty()){
        Text(
            text = "Questions are not available",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(12.dp)
        )
        return
    }

    val count = countList.getOrNull(currentIndex) ?: countList[0]

    var progress by remember {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(currentIndex) {
        progress = if (currentIndex < countList.size) {
            (currentIndex + 1).toFloat() / countList.size.toFloat()
        } else {
            1f
        }
    }

    var optionsList by remember {
        mutableStateOf(generateOptionsWithCorrectAnswer(correctNumber = count))
    }

    LaunchedEffect(currentIndex) {
        optionsList = generateOptionsWithCorrectAnswer(correctNumber = countList[currentIndex])
    }


    Box(
        modifier =modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        LazyColumn (
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            item{
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                ) {
                    Text(
                        text = "Count and Match",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Spacer(Modifier.padding(16.dp))

                    Text(
                        text = "Question ${currentIndex + 1}/${countList.size}",
                        style = MaterialTheme.typography.titleMedium,
                    )


                    AppLinearProgressIndicator(
                        progress = progress
                    )

                }
            }


            item {
                Spacer(Modifier.size(20.dp))
            }

            item{
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier

                ) {

                    AppShowInstructions(
                        instructionAudio = R.raw.count_the_balls_then_select_your_answer,
                        instructionsTitle = "Count the Balls",
                        instructionsDescription = "Count the balls shown below, then select your answer.",
                        content = {
                            FlowRow(
                                horizontalArrangement = Arrangement.Center,
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                itemVerticalAlignment = Alignment.CenterVertically,
                                maxItemsInEachRow = 5
                            ) {
                                repeat(count) { index ->
                                    CountItem()
                                }
                            }
                        }
                    )

                    Divider(
                        color = MaterialTheme.colorScheme.onBackground,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                            OptionButton(
                                number = optionsList[0],
                                onClick = { onSelectCount(optionsList[0]) },
                                isSelected = selectedCount == optionsList[0]
                            )
                            OptionButton(
                                number = optionsList[1],
                                onClick = { onSelectCount(optionsList[1]) },
                                isSelected = selectedCount == optionsList[1]
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                            OptionButton(
                                number = optionsList[2],
                                onClick = { onSelectCount(optionsList[2]) },
                                isSelected = selectedCount == optionsList[2]
                            )
                            OptionButton(
                                number = optionsList[3],
                                onClick = { onSelectCount(optionsList[3]) },
                                isSelected = selectedCount == optionsList[3]
                            )
                        }
                    }


                }

            }


        }


        AppButton(
            onClick = onSubmit,
            enabled = selectedCount != null,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Text(
                "Submit",
                color = MaterialTheme.colorScheme.onSecondary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }

    }



}

@Composable
fun OptionButton(
    number: Int,
    onClick: () -> Unit,
    isSelected: Boolean = false,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f) else Color(0xFF102C57),
        ),
        modifier = Modifier.size(100.dp)
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFFFFC107),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CountItem(){
    var isClicked by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            isClicked = !isClicked
        },
        modifier = Modifier
            .padding(4.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.football), // You must add this drawable
            contentDescription = "Football",
            modifier = Modifier
                .size(60.dp),
            tint = if (!isClicked) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
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
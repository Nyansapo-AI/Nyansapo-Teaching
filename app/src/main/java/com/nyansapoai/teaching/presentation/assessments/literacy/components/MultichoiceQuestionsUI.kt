package com.nyansapoai.teaching.presentation.assessments.literacy.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nyansapoai.teaching.domain.models.assessments.literacy.MultipleChoices
import com.nyansapoai.teaching.domain.models.assessments.literacy.QuestionData
import com.nyansapoai.teaching.presentation.common.components.AppLinearProgressIndicator

@Composable
fun MultichoiceQuestionsUI(
    modifier: Modifier = Modifier,
    currentIndex: Int,
    story: String,
    questionsList: List<QuestionData>,
    selectedChoice: String?,
    onSelectedChoiceChange: (String) -> Unit,
    onSetOptionsList: (List<String>) -> Unit,
    onSubmitMultipleChoices: () -> Unit
) {


    if (questionsList.isEmpty()){
        Text(
            text = "Questions are not available.",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        return
    }

    var progress by remember {
        mutableFloatStateOf(0f)
    }

    var showStory by remember { mutableStateOf(false) }

    val choiceList = remember(currentIndex) {
        (listOf(questionsList[currentIndex].multipleChoices.correctChoices.random()) +
                questionsList[currentIndex].multipleChoices.wrongChoices.shuffled().take(2)).shuffled()
    }

    LaunchedEffect(currentIndex) {
        progress = if (currentIndex < questionsList.size) {
            (currentIndex + 1).toFloat() / questionsList.size.toFloat()
        } else {
            1f
        }
    }

    LaunchedEffect(choiceList) {
        onSetOptionsList(choiceList)
    }


    when(showStory){
        true -> {
            StoryUI(
                story = story ,
                onClose = { showStory = false }
            )
        }
        false -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(60.dp),
                modifier = modifier
//                    .fillMaxSize()
                    .widthIn(max = 600.dp)
                    .padding(16.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Story Questions",
                            style = MaterialTheme.typography.headlineSmall,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        TextButton(
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            ),
//                            shape = RoundedCornerShape(10),
                            onClick = {
                                showStory = !showStory
                            }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier,
                            ) {

                                Text(
                                    text = "Story",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                    }


                    AppLinearProgressIndicator(
                        progress = progress
                    )


                }

                MultichoiceQuestionItemUI(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    question = questionsList[currentIndex].question,
                    choices = choiceList,
                    selectedChoice = selectedChoice,
                    canSubmit = selectedChoice != null,
                    onChoiceSelected = { choice -> onSelectedChoiceChange(choice) },
                    onSubmit = onSubmitMultipleChoices
                )

            }

        }
    }


}
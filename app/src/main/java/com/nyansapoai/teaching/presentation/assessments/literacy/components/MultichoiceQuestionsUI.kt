package com.nyansapoai.teaching.presentation.assessments.literacy.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nyansapoai.teaching.domain.models.assessments.literacy.MultipleChoices
import com.nyansapoai.teaching.domain.models.assessments.literacy.QuestionData
import com.nyansapoai.teaching.presentation.common.components.AppLinearProgressIndicator

@Composable
fun MultichoiceQuestionsUI(
    modifier: Modifier = Modifier,
    currentIndex: Int = 0,
    story: String = "Our school opened on fifth May. Many children came to school early. They were all happy to be back. Peter and Jim were also there. The two boys are my good friends. Peter told us he visited his aunt. She bought him a black pair of shoes. Jim wanted to wear the new shoes. Peter told him that his feet were dirty. Jim got angry and shouted at Peter. Some children came to look at them. They made a lot of noise. The teacher came from the office. She told Peter and Jim not to fight.",
    questionsList: List<QuestionData> = listOf(
        QuestionData(
            question = "When did our school open?",
            multipleChoices = MultipleChoices(
                correctChoices = listOf(
                    " On fifth May. ",
                    " Our school opened on fifth May. "
                ),
                wrongChoices = listOf(
                    " Our school opened today. ",
                    "Our school opened on third May. ",
                    " Our school opened on second May. ",
                    " Our school opened on first May. "
                )
            )
        ),
        QuestionData(
            question = "What did the teacher do when she came from the office?",
            multipleChoices = MultipleChoices(
                correctChoices = listOf(
                    "She told Peter and Jim not to fight. ",
                    " The teacher told Peter and Jim not to fight. "
                ),
                wrongChoices = listOf(
                    "The teacher caned Peter and Jim. ",
                    "The teacher took Peter's shoes. ",
                    "The teacher told Peter and Jim to go home. ",
                    " The teacher told Peter and Jim to go to her office. "
                )
            )
        )
    )

) {

    var progress by remember {
        mutableFloatStateOf(0f)
    }

    var selectedChoice by remember { mutableStateOf<String?>(null) }

    var showStory by remember { mutableStateOf(false) }


    LaunchedEffect(currentIndex) {
        progress = if (currentIndex < questionsList.size) {
            (currentIndex + 1).toFloat() / questionsList.size.toFloat()
        } else {
            1f
        }
    }


    val choiceList = remember(currentIndex) {
        (listOf(questionsList[currentIndex].multipleChoices.correctChoices.random()) +
                questionsList[currentIndex].multipleChoices.wrongChoices.shuffled().take(2)).shuffled()
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
                verticalArrangement = Arrangement.spacedBy(40.dp),
                modifier = modifier
                    .fillMaxSize()
                    .widthIn(max = 600.dp)
                    .padding(16.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Text(
                        text = "Story",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )


                    AppLinearProgressIndicator(
                        progress = progress
                    )

                    TextButton(
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
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

                MultichoiceQuestionItemUI(
                    modifier = Modifier.fillMaxWidth(),
                    question = questionsList[currentIndex].question,
                    choices = choiceList,
                    selectedChoice = selectedChoice,
                    canSubmit = selectedChoice != null,
                    onChoiceSelected = { choice -> selectedChoice = choice },
                    onSubmit = { /* Handle submit action */ }
                )

            }

        }
    }


}
package com.nyansapoai.teaching.presentation.assessments.literacy.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.domain.models.assessments.literacy.MultipleChoices
import com.nyansapoai.teaching.domain.models.assessments.literacy.QuestionData
import com.nyansapoai.teaching.presentation.common.audio.AppLocalAudioPlayer
import com.nyansapoai.teaching.presentation.common.components.AppShowInstructions
import kotlinx.coroutines.delay

@Composable
fun ListeningQuestionUI(
    modifier: Modifier = Modifier,
    currentIndex: Int = 0,
    story: String=  """
                Kache and her friend Tunu go to the playground every evening. 
                One evening, they found a hurt bird on the ground. 
                They took the bird home and made a nest in a box with leaves. 
                They gave the bird food and water. The bird got stronger and flew around the house. 
                One day, the window was left open, and the bird was gone.
            """.trimIndent(),
    questionsList: List<QuestionData> =  listOf(
        QuestionData(
            question = "What is the name of Kache’s friend?",
            audio = R.raw.what_is_the_name_of_kaches_friend,
            multipleChoices = MultipleChoices(
                correctChoices = listOf("Tunu"),
                wrongChoices = listOf("Kisa", "Amina", "Musa")
            )
        ),
        QuestionData(
            question = "What do Kache and Tunu do every evening?",
            audio = R.raw.what_do_kache_and_tunu_do_every_evening,
            multipleChoices = MultipleChoices(
                correctChoices = listOf("They go to the playground."),
                wrongChoices = listOf("They go to school.", "They feed the cows.", "They visit the market.")
            )
        ),
        QuestionData(
            question = "What did the girls make a nest with?",
            audio = R.raw.what_did_the_girls_make_a_nest_with,
            multipleChoices = MultipleChoices(
                correctChoices = listOf("Leaves."),
                wrongChoices = listOf("Paper.", "Plastic.", "Stones.")
            )
        ),
        QuestionData(
            question = "Why do you think the bird was nowhere to be found?",
            audio = R.raw.why_do_you_think_the_bird_was_nowhere_to_be_found,
            multipleChoices = MultipleChoices(
                correctChoices = listOf("Because it flew away."),
                wrongChoices = listOf("Because a cat took it.", "Because it was hiding.", "Because it fell asleep.")
            )
        )
    ),
    title: String = "Two girls and a bird",
    selectedChoice: String? = null,
    onSelectedChoiceChange: (String) -> Unit = {},
    onSetOptionsList: (List<String>) -> Unit = {},
    onSubmitMultipleChoices: () -> Unit = {},
    storyAudio: Int? = R.raw.two_friends_and_bird,
) {

    if (storyAudio == null){
        Text(
            text = "The Story is not available.",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
        )
        return
    }

    var showStory by remember { mutableStateOf(true) }
    var isStoryPlaying by remember { mutableStateOf(false) }
    var isPlayingInstructions by remember { mutableStateOf(true) }

    LaunchedEffect(isPlayingInstructions) {
        if (!isPlayingInstructions){
            delay(1500)

            isStoryPlaying = true
        }
    }

    AnimatedVisibility(
        visible = isStoryPlaying && !isPlayingInstructions
    ) {
        AppLocalAudioPlayer(
            audio = storyAudio,
            hasFinishedPlaying = { hasCompleted ->
                showStory = !hasCompleted
                isStoryPlaying = !hasCompleted
            }
        )
    }

    AnimatedContent(
        targetState = showStory,
        modifier = modifier
            .fillMaxSize()
    ) { playing ->

        when(playing) {
            true -> {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = modifier
                        .fillMaxWidth()
                )
                {
                    Text(
                        text = title,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 48.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.ExtraBold
                        ),
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )

                    AppShowInstructions(
                        showInstructions = isPlayingInstructions,
                        instructionAudio = R.raw.listening_story_instruction,
                        onChangeShow = {},
                        hasCompletedPlaying = { hasCompleted ->
                            isPlayingInstructions = !hasCompleted
                        },
                        instructionsTitle = "Listen to The Story",
                        instructionsDescription = "Listen carefully to the story being read aloud. Once you have finished listening, you will be asked some questions about it.",
                        modifier = Modifier
                    )
                    {
                        LazyColumn {
                            item {
                                Text(
                                    text = story ,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontSize = 40.sp,
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .padding(bottom = 20.dp)
                                )
                            }
                        }
                    }
                }


            }
            false -> {
                MultichoiceQuestionsUI(
                    currentIndex = currentIndex,
                    story = story,
                    questionsList = questionsList,
                    selectedChoice = selectedChoice,
                    onSelectedChoiceChange = onSelectedChoiceChange,
                    onSetOptionsList = onSetOptionsList,
                    onSubmitMultipleChoices = onSubmitMultipleChoices,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }

    }

}


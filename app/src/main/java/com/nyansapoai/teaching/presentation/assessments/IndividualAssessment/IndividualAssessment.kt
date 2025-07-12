package com.nyansapoai.teaching.presentation.assessments.IndividualAssessment

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.presentation.schools.LearningLevelDescription
import com.nyansapoai.teaching.presentation.schools.components.LearningLevelItem
import com.nyansapoai.teaching.presentation.common.components.AppCircularLoading
import com.nyansapoai.teaching.navigation.ConductAssessmentPage
import com.nyansapoai.teaching.utils.ResultStatus
import org.koin.androidx.compose.koinViewModel

@Composable
fun IndividualAssessmentRoot(
    assessmentId: String
) {

    val viewModel = koinViewModel<IndividualAssessmentViewModel>()

    LaunchedEffect(true) {
        viewModel.getAssessmentById(assessmentId = assessmentId)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    IndividualAssessmentScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndividualAssessmentScreen(
    state: IndividualAssessmentState,
    onAction: (IndividualAssessmentAction) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.assessmentState.data?.name ?: "Unknown Assessment",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = state.assessmentState.status,
            transitionSpec = { fadeIn() togetherWith fadeOut() } ,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) { status ->
            when(status){
                ResultStatus.INITIAL ,
                ResultStatus.LOADING -> {
                    AppCircularLoading()
                }
                ResultStatus.SUCCESS -> {
                    state.assessmentState.data?.let{ assessment ->
                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            item {
                                state.assessmentState.data.level_distribution.let { assessmentLevelDistributions ->
                                    if (assessmentLevelDistributions.isEmpty()){
                                        ElevatedCard(
                                            colors = CardDefaults.outlinedCardColors(
                                                containerColor = MaterialTheme.colorScheme.tertiary
                                            ),
                                            elevation = CardDefaults.cardElevation(
                                                defaultElevation = 12.dp
                                            ),
                                            modifier = Modifier
                                                .heightIn(max = 200.dp)
                                                .widthIn(max = 420.dp)
                                                .padding(12.dp)
                                        ) {
                                            Text(
                                                "no data available yet",
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp)
                                            )
                                        }
                                        return@item
                                    }

                                    LearningLevelItem(
                                        modifier = Modifier
                                            .padding(12.dp)
                                            .heightIn(min = 200.dp)
                                            .widthIn(max = 420.dp),
                                        levelDescription = LearningLevelDescription(state.assessmentState.data.type, totalStudents = state.assessmentState.data.assigned_students.size)
                                    )


                                }
                            }

                            stickyHeader {
                                Text(
                                    text = "Students",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                            }

                            items(items = assessment.assigned_students, key = { it.student_id }) { student ->
                                ElevatedCard(
                                    colors = CardDefaults.outlinedCardColors(
                                        containerColor = MaterialTheme.colorScheme.tertiary,
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 12.dp
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .widthIn(max = 420.dp)
                                        .padding(horizontal = 12.dp)
                                        .clickable(
                                            onClick = {
                                                navController.navigate(ConductAssessmentPage(
                                                    assessmentId = assessment.id,
                                                    studentId = student.student_id,
                                                    assessmentType = assessment.type,
                                                    assessmentNo = assessment.assessmentNumber
                                                ))
                                            }
                                        )
                                        .border(
                                            width = 2.dp,
                                            color = Color.Green,
                                            shape = CardDefaults.elevatedShape
                                        )

                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp)

                                    ) {
                                        Text(
                                            text = student.student_name,
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier
                                        )

                                        TextButton(
                                            onClick = {

                                                navController.navigate(ConductAssessmentPage(
                                                    assessmentId = assessment.id,
                                                    studentId = student.student_id,
                                                    assessmentType = assessment.type,
                                                    assessmentNo = assessment.assessmentNumber
                                                ))
                                            },
                                            colors = ButtonDefaults.textButtonColors(
                                                contentColor = MaterialTheme.colorScheme.onBackground,
                                            ),
                                        ) {
                                            Text(
                                                text = student.competence?.let { "$it" } ?: "Assess",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                                                modifier = Modifier
//                                                .padding(16.dp)
//                                                .weight(1f)
                                            )

                                        }

                                    }
                                }
                            }
                        }

                    }
                }
                ResultStatus.ERROR -> {

                }
            }
        }
    }

}
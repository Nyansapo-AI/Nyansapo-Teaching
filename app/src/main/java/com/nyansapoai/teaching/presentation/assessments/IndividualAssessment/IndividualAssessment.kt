package com.nyansapoai.teaching.presentation.assessments.IndividualAssessment

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.domain.models.students.NyansapoStudent
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.presentation.common.components.AppCircularLoading
import com.nyansapoai.teaching.navigation.ConductAssessmentPage
import com.nyansapoai.teaching.presentation.assessments.IndividualAssessment.composables.AssessmentsStatUI
import com.nyansapoai.teaching.presentation.onboarding.components.OptionsItemUI
import com.nyansapoai.teaching.utils.ResultStatus
import org.koin.androidx.compose.koinViewModel
import kotlin.text.ifEmpty

@Composable
fun IndividualAssessmentRoot(
    assessmentId: String
) {

    val viewModel = koinViewModel<IndividualAssessmentViewModel>()
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
    onAction: (IndividualAssessmentAction) -> Unit
) {

    val context = LocalContext.current

    LaunchedEffect(state.assessmentState) {
        onAction.invoke(
            IndividualAssessmentAction.OnGetCompletedAssessments(assessmentId = state.assessmentState.data?.id ?: "")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "Back",
                        )
                    }
                },
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
        },
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) { innerPadding ->

        AnimatedVisibility(
            visible = state.isLoading
        ) {
            AppCircularLoading()
        }

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
                                AssessmentsStatUI(
                                    total = state.assessmentState.data.assigned_students.size,
                                    completed = state.assessmentState.data.assigned_students.filter { it.has_done }.size,
                                    modifier = Modifier
                                        .padding(16.dp)
                                )
                            }

                            stickyHeader {
                                Text(
                                    text = "Learners",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.primary)
                                        .padding(horizontal = 12.dp)
                                )
                            }
                            item {
                                val grades = listOf<Int?>(null, 1, 2, 3, 4, 5,6,7,8,9)

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(items = grades){ item ->
                                        OptionsItemUI(
                                            text = item?.let{"Grade $item"} ?: "All",
                                            isSelected = item == state.selectedGrade,
                                            onClick = {
                                                onAction.invoke(
                                                    IndividualAssessmentAction.OnSetGrade(grade = item)
                                                )
                                            },
                                            modifier = Modifier
                                                .then(if(item == grades.first()) Modifier.padding(start = 16.dp) else Modifier)
                                                .then(if(item == grades.last()) Modifier.padding(end = 16.dp) else Modifier)
                                        )
                                    }

                                }
                            }


                            item {
                                if (state.studentsList.isEmpty()){
                                    Text(
                                        text = "No Student assigned",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Left
                                    )
                                }
                                return@item
                            }


                            items(
                                items = state.studentsList.sortedWith(compareBy { !it.has_done }).reversed(),
                                key = { it.id }
                            ) { student ->
                                AssignedLearnersItem(
                                    student = student,
                                    hasDone = student.has_done || student.id in state.completedAssessments.map { it.student_id },
                                    doneClick = {
                                        Toast.makeText(context, "${student.first_name} ${student.last_name} has already done the assessment", Toast.LENGTH_SHORT).show()
                                    },
                                    notDoneClick = {
                                        navController.navigate(ConductAssessmentPage(
                                            assessmentId = assessment.id,
                                            studentId = student.id,
                                            assessmentType = assessment.type,
                                            assessmentNo = assessment.assessmentNumber,
                                            studentName = student.first_name + " " + student.last_name
                                        ))
                                    }
                                )
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


@Composable
fun AssignedLearnersItem(
    modifier: Modifier = Modifier,
    student: NyansapoStudent,
    hasDone: Boolean,
    doneClick: () -> Unit,
    notDoneClick: () -> Unit,
){
    ElevatedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (student.has_done) Color.Green.copy(alpha = 0.2f) else MaterialTheme.colorScheme.tertiary,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 420.dp)
            .padding(horizontal = 12.dp)
            .clickable(
                onClick = {
                    if (hasDone){
                        doneClick()
                        return@clickable
                    }
                    notDoneClick()
                }
            )
            .border(
                width = 2.dp,
                color = if (hasDone)Color.Green else Color.Transparent,
                shape = CardDefaults.elevatedShape
            )

    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)

        ) {
            Text(
                text = student.name.ifEmpty { "${student.first_name} ${student.last_name}" },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
            )

            TextButton(
                onClick = {

                    if (hasDone){
                        doneClick()
                        return@TextButton
                    }
                    notDoneClick()

                    /*
                    navController.navigate(
                        LiteracyResultsPage(
                            assessmentId = assessment.id,
                            studentId = student.id
                        )
                    )*/


                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
            ) {

                if  (hasDone){
                    Image(
                        painter = painterResource(R.drawable.done_3_),
                        contentDescription = "has completed the assessment",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(40.dp)
                            .graphicsLayer(
                                rotationZ = -0.2f
                            )
                    )
                    return@TextButton
                }
                Text(
                    text = "Start",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                    modifier = Modifier
                )

            }

        }
    }

}
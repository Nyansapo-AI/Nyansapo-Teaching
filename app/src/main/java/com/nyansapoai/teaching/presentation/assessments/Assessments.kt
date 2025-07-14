package com.nyansapoai.teaching.presentation.assessments

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.presentation.assessments.components.AssessmentItem
import com.nyansapoai.teaching.presentation.common.components.AppCircularLoading
import com.nyansapoai.teaching.navigation.CreateAssessmentsPage
import com.nyansapoai.teaching.navigation.IndividualAssessmentPage
import com.nyansapoai.teaching.utils.ResultStatus
import org.koin.androidx.compose.koinViewModel

@Composable
fun AssessmentsRoot() {

    val viewModel = koinViewModel<AssessmentsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    AssessmentsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun AssessmentsScreen(
    state: AssessmentsState,
    onAction: (AssessmentsAction) -> Unit,
) {


    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = stringResource(R.string.assessment),
                    style = MaterialTheme.typography.headlineSmall,
                )

                TextButton (
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        navController.navigate(CreateAssessmentsPage)
                    },
                    modifier = Modifier
                        .width(180.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.add),
                            contentDescription = "Click to add assessment",
                        )

                        Text(
                            text = stringResource(R.string.add_assessment),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

        }
    ) { innerPadding ->

        AnimatedContent(
            targetState = state.assessmentListState.status,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
        ) { status ->
            when(status){
                ResultStatus.INITIAL ,
                ResultStatus.LOADING -> {
                    AppCircularLoading()
                }
                ResultStatus.SUCCESS -> {
                    state.assessmentListState.data?.let { assessments ->
                        if (assessments.isEmpty()){

                            Text(
                                text = "No Assessments created yet",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            )
                            return@let
                        }
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 8.dp)
                        ) {
                            items(items =assessments, key = {it.id}, ) { assessment ->
                                AssessmentItem(
                                    assessment = assessment,
                                    onClick = {
                                        navController.navigate(IndividualAssessmentPage(
                                            assessmentId = assessment.id
                                        ))
                                    }
                                )
                            }
                        }

                    }


                }
                ResultStatus.ERROR -> {}
            }
        }


    }



}
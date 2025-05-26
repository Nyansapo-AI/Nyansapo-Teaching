package com.nyansapoai.teaching.presentation.assessments.createAssessment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nyansapoai.teaching.presentation.common.components.AppButton
import com.nyansapoai.teaching.presentation.common.components.AppTextField
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateAssessmentsRoot() {

    val viewModel = koinViewModel<CreateAssessmentsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    CreateAssessmentsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAssessmentsScreen(
    state: CreateAssessmentsState,
    onAction: (CreateAssessmentsAction) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text(
                        text = "Create Assessment",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { innerPadding ->


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
            ) {

                item {
                    AppTextField(
                        value = state.name,
                        onValueChanged = { onAction(CreateAssessmentsAction.SetName(it)) },
                        label = "Assessment Name",
                        placeholder = "Enter assessment name",
                    )
                }

                item {
                    AppTextField(
                        value = state.type,
                        onValueChanged = { onAction(CreateAssessmentsAction.SetType(it)) },
                        label = "Assessment Type",
                        placeholder = "Enter assessment type",
                    )
                }

                item {
                    AppTextField(
                        value = state.startLevel,
                        onValueChanged = { onAction(CreateAssessmentsAction.SetStartLevel(it)) },
                        label = "Start Level",
                        placeholder = "Enter start level",
                    )
                }

                item {
                    AppTextField(
                        value = state.assessmentNumber.toString(),
                        onValueChanged = {
                            onAction(
                                CreateAssessmentsAction.SetAssessmentNumber(
                                    it.toIntOrNull() ?: 1
                                )
                            )
                        },
                        label = "Assessment Number",
                        placeholder = "Enter assessment number",
                    )
                }

                item {
                    AppTextField(
                        value = "${state.assignedStudents.size}",
                        onValueChanged = {
                            // Assuming a simple comma-separated input for assigned students
                            //                        onAction(CreateAssessmentsAction.AddAssignedStudent(it.split(",").map { it.trim() }))
                        },
                        label = "Assigned Students",
                        placeholder = "Enter assigned students (comma separated)",
                    )
                }

            }

            AppButton(
                onClick = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = "Save Assessment",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }

}
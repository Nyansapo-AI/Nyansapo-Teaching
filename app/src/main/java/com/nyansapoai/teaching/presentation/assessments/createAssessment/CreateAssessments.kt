package com.nyansapoai.teaching.presentation.assessments.createAssessment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.presentation.common.components.AppButton
import com.nyansapoai.teaching.presentation.common.components.AppDropDownItem
import com.nyansapoai.teaching.presentation.common.components.AppDropDownMenu
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
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .imePadding()
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
                    /*
                    AppTextField(
                        value = state.type,
                        onValueChanged = { onAction(CreateAssessmentsAction.SetType(it)) },
                        label = "Assessment Type",
                        placeholder = "Enter assessment type",
                    )

                     */

                    AppDropDownMenu(
                        expanded = state.isTypeDropDownExpanded,
                        label = "Assessment Type",
                        placeholder = "Select assessment type",
                        value = state.type,
                        onClick = { onAction(CreateAssessmentsAction.ToggleTypeDropDown(isExpanded = !state.isTypeDropDownExpanded)) },
                        error = null
                    ) {
                        state.assessmentTypeList.forEach { type ->
                            AppDropDownItem(
                                item = type,
                                isSelected = type == state.type,
                                onClick = { onAction(CreateAssessmentsAction.SetType(type)) }
                            )
                        }
                    }

                }

                /*
                item {
                    AppDropDownMenu(
                        expanded = state.isStartLevelDropDownExpanded,
                        label = "Start Level",
                        placeholder = "Select starting level",
                        value = state.startLevel,
                        onClick = { onAction(CreateAssessmentsAction.ToggleStartLevelDropDown(isExpanded = !state.isStartLevelDropDownExpanded)) },
                        error = null
                    ) {
                        state.selectedStartingLevel.forEach {
                            AppDropDownItem(
                                item = it,
                                isSelected = it == state.startLevel,
                                onClick = { onAction(CreateAssessmentsAction.SetStartLevel(it)) }
                            )
                        }
                    }
                }

                 */

                item {
                    AppDropDownMenu(
                        expanded = state.isAssessmentNumberDropDownExpanded,
                        label = "Assessment Number",
                        placeholder = "Select assessment number",
                        value = state.assessmentNumber.toString(),
                        onClick = { onAction(CreateAssessmentsAction.ToggleAssessmentNumberDropDown(isExpanded = !state.isAssessmentNumberDropDownExpanded)) },
                        error = null
                    ) {

                        state.assessmentNumberList.forEach { number ->
                            AppDropDownItem(
                                item = number.toString(),
                                isSelected = number == state.assessmentNumber,
                                onClick = {
                                    onAction(
                                        CreateAssessmentsAction.SetAssessmentNumber(
                                            number
                                        )
                                    )
                                }
                            )
                        }
                    }

                }

                item {
                    AppDropDownMenu(
                        expanded = state.isStudentListDropDownExpanded,
                        label = "Assigned Students",
                        placeholder = "Select students to assign",
                        value = if (state.assignedStudents.size > 1) "${state.assignedStudents.size} student" else "${state.assignedStudents.size} students",
                        onClick = {
                            onAction(CreateAssessmentsAction.ToggleStudentListDropDown(isExpanded = !state.isStudentListDropDownExpanded))
                        },
                        error = null
                    ) {
                        state.studentList.forEach { student ->
                            AppDropDownItem(
                                item = student.student_name,
                                isSelected = student in state.assignedStudents,
                                onClick = {
                                    onAction(CreateAssessmentsAction.AddAssignedStudent(student = student))
                                }
                            )
                        }
                    }
                }

                item {
                    Spacer(Modifier.padding(60.dp))
                }

            }

           Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
           ) {
               AppButton(
                   enabled = state.canSubmit,
                   onClick = {
                       onAction(CreateAssessmentsAction.SubmitAssessment(
                           onSuccess = {
                               navController.popBackStack()
                           }
                       ))
                   },
                   modifier = Modifier
                       .fillMaxWidth()
                       .align(Alignment.Center)
               ) {
                   Text(
                       text = "Save Assessment",
                       style = MaterialTheme.typography.titleMedium,
                       fontWeight = FontWeight.Bold
                   )
               }

           }

        }
    }

}
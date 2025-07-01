package com.nyansapoai.teaching.presentation.assessments.createAssessment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.domain.models.assessments.AssignedStudent
import com.nyansapoai.teaching.presentation.common.snackbar.SnackBarHandler
import com.nyansapoai.teaching.presentation.common.snackbar.SnackBarItem
import com.nyansapoai.teaching.utils.ResultStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateAssessmentsViewModel(
    private val assessmentRepository: AssessmentRepository,
    private val snackBarHandler: SnackBarHandler
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(CreateAssessmentsState())

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CreateAssessmentsState()
        )

    fun onAction(action: CreateAssessmentsAction) {
        when (action) {
            is CreateAssessmentsAction.AddAssignedStudent -> {

                _state.update { currentState ->
                    val isDuplicate = currentState.assignedStudents.any { it.student_id == action.student.student_id }
                    if (!isDuplicate) {
                        currentState.copy(assignedStudents = currentState.assignedStudents + action.student)
                    } else {
                        currentState
                    }
                }
            }
            is CreateAssessmentsAction.SetAssessmentNumber -> {
                _state.update { it.copy(assessmentNumber = action.assessmentNumber) }
            }
            is CreateAssessmentsAction.SetName -> {
                _state.update { it.copy(name = action.name) }
            }
            is CreateAssessmentsAction.SetStartLevel -> {
                _state.update { it.copy(startLevel = action.startLevel, isStartLevelDropDownExpanded = false) }
            }
            is CreateAssessmentsAction.SetType -> {
                _state.update {
                    it.copy(
                        type = action.type,
                        isTypeDropDownExpanded = false,
                    )
                }
            }

            is CreateAssessmentsAction.ToggleAssessmentNumberDropDown -> {
                _state.update { it.copy(isAssessmentNumberDropDownExpanded = action.isExpanded) }
            }
            is CreateAssessmentsAction.ToggleStartLevelDropDown -> {
                _state.update { it.copy(isStartLevelDropDownExpanded = action.isExpanded) }
            }
            is CreateAssessmentsAction.ToggleTypeDropDown -> {
                _state.update { it.copy(isTypeDropDownExpanded = action.isExpanded) }
            }

            is CreateAssessmentsAction.ToggleStudentListDropDown -> {
                _state.update { it.copy(isStudentListDropDownExpanded = action.isExpanded) }
            }

            is CreateAssessmentsAction.SubmitAssessment -> {
                createAssessment(
                    name = _state.value.name,
                    type = _state.value.type,
                    startLevel = _state.value.startLevel,
                    assessmentNumber = _state.value.assessmentNumber,
                    assignedStudents = _state.value.assignedStudents
                )
            }
        }
    }

    private fun createAssessment(
        name: String,
        type: String,
        startLevel: String,
        assessmentNumber: Int,
        assignedStudents: List<AssignedStudent> // Assuming AssignedStudent is a String for simplicity
    ) {
        viewModelScope.launch {
            val result = assessmentRepository.createAssessment(
                name = name,
                type = type,
                startLevel = startLevel,
                assessmentNumber = assessmentNumber,
                assignedStudents = assignedStudents
            )

            when (result.status){
                ResultStatus.INITIAL ,
                ResultStatus.LOADING -> {}
                ResultStatus.SUCCESS -> {

                    _state.update {
                        it.copy(
                            name = "",
                            type = "",
                            startLevel = "",
                            assessmentNumber = 1,
                            assignedStudents = emptyList(),
                            isAssessmentNumberDropDownExpanded = false,
                            isStartLevelDropDownExpanded = false,
                            isTypeDropDownExpanded = false,
                            isStudentListDropDownExpanded = false
                        )
                    }


                    snackBarHandler.showSnackBarNotification(
                        snackBarItem = SnackBarItem(
                            message = "Assessment created successfully",
                            isError = false
                        )
                    )

                }
                ResultStatus.ERROR -> {
                    snackBarHandler.showSnackBarNotification(
                        snackBarItem = SnackBarItem(
                            message = result.message ?: "Failed to create assessment",
                            isError = true
                        )
                    )
                }
            }
        }
    }

    init {
        /*
        createAssessment(
            name = "Sample Assessment",
            type = "Quiz",
            startLevel = "Beginner",
            assessmentNumber = 1,
            assignedStudents = listOf("student1", "student2") // Example student IDs
        )*/
    }

}
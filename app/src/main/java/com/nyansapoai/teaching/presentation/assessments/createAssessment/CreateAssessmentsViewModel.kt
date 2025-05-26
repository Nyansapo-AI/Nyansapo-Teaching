package com.nyansapoai.teaching.presentation.assessments.createAssessment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateAssessmentsViewModel(
    private val assessmentRepository: AssessmentRepository
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

                _state.value = _state.value.copy(
                    assignedStudents = _state.value.assignedStudents + action.studentId
                )
            }
            is CreateAssessmentsAction.SetAssessmentNumber -> {
                _state.update { it.copy(assessmentNumber = action.assessmentNumber) }
            }
            is CreateAssessmentsAction.SetName -> {
                _state.update { it.copy(name = action.name) }
            }
            is CreateAssessmentsAction.SetStartLevel -> {
                _state.update { it.copy(startLevel = action.startLevel) }
            }
            is CreateAssessmentsAction.SetType -> {
                _state.update { it.copy(type = action.type) }
            }
        }
    }

    fun createAssessment(
        name: String,
        type: String,
        startLevel: String,
        assessmentNumber: Int,
        assignedStudents: List<String> // Assuming AssignedStudent is a String for simplicity
    ) {
        viewModelScope.launch {
            val result = assessmentRepository.createAssessment(
                name = name,
                type = type,
                startLevel = startLevel,
                assessmentNumber = assessmentNumber,
                assignedStudents = emptyList() // Convert to AssignedStudent if needed
            )
            // Handle result (success or failure)
        }
    }

    init {
        createAssessment(
            name = "Sample Assessment",
            type = "Quiz",
            startLevel = "Beginner",
            assessmentNumber = 1,
            assignedStudents = listOf("student1", "student2") // Example student IDs
        )
    }

}
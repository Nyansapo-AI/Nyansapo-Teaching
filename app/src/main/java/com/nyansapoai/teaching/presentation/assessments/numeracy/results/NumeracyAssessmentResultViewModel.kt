package com.nyansapoai.teaching.presentation.assessments.numeracy.results

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.navigation.AssessmentResultsPage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class NumeracyAssessmentResultViewModel(
    savedStateHandle: SavedStateHandle,
    private val assessmentRepository: AssessmentRepository
) : ViewModel() {

    private val numeracyAssessmentResultPageArgs = savedStateHandle.toRoute<AssessmentResultsPage>()
    private val _state = MutableStateFlow(NumeracyAssessmentResultState())
    val state = combine(
        _state,
        assessmentRepository.fetchNumeracyAssessmentResults(assessmentId = numeracyAssessmentResultPageArgs.assessmentId, studentId = numeracyAssessmentResultPageArgs.studentId)
    ){ currentState, results ->

        currentState.copy(
            count_and_match = results.data?.numeracy_results?.count_and_match ?: emptyList(),
            additions = results.data?.numeracy_results?.number_operations?.filter { it.type == "addition" } ?: emptyList(),
            subtractions = results.data?.numeracy_results?.number_operations?.filter { it.type == "subtraction" } ?: emptyList(),
            multiplications = results.data?.numeracy_results?.number_operations?.filter { it.type == "multiplication" } ?: emptyList(),
            divisions = results.data?.numeracy_results?.number_operations?.filter { it.type == "division" } ?: emptyList(),
            word_problem = results.data?.numeracy_results?.word_problem ?: emptyList(),
            number_recognition = results.data?.numeracy_results?.number_recognition ?: emptyList(),
            isLoading = false,
            error = null
        )

    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = NumeracyAssessmentResultState()
        )

    fun onAction(action: NumeracyAssessmentResultAction) {
        when (action) {
            is NumeracyAssessmentResultAction.OnSelectScreenshotImage -> {
                _state.update { it.copy(screenshotImage = action.imageUrl) }
            }


            is NumeracyAssessmentResultAction.OnSelectedNumeracyRecognitionResult -> {
                _state.update { it.copy(selectedNumberRecognition = action.numberRecognition) }
            }
        }
    }

}
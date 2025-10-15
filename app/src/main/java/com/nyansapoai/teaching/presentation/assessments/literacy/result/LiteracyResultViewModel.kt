package com.nyansapoai.teaching.presentation.assessments.literacy.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.navigation.LiteracyResultsPage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class LiteracyResultViewModel(
    savedStateHandle: SavedStateHandle,
    private val assessmentRepository: AssessmentRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val literacyRouteArgs = savedStateHandle.toRoute<LiteracyResultsPage>()

    private val _state = MutableStateFlow(LiteracyResultState())
    val state = combine(
        _state,
        assessmentRepository.fetchLiteracyAssessmentResults(
            assessmentId = literacyRouteArgs.assessmentId,
            studentId = literacyRouteArgs.studentId
        )
    ) { currentState, results ->
        currentState.copy(
            results = results.data,
            stories = results.data?.literacy_results?.reading_results?.filter { (content, metadata) -> metadata?.type == "Story" } ?: emptyList(),
            letters = results.data?.literacy_results?.reading_results?.filter { (content, metadata) -> metadata?.type == "Letter" } ?: emptyList(),
            words = results.data?.literacy_results?.reading_results?.filter { (content, metadata) -> metadata?.type == "Word" } ?: emptyList(),
            paragraphs = results.data?.literacy_results?.reading_results?.filter { (content, metadata) -> metadata?.type == "Paragraph" } ?: emptyList(),
            multipleChoiceQuestions = results.data?.literacy_results?.multiple_choice_questions ?: emptyList(),
            isLoading = false,
            error = null
        )
    }
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LiteracyResultState()
        )

    fun onAction(action: LiteracyResultAction) {
        when (action) {
            is LiteracyResultAction.OnSelectAudioUrl -> {
                _state.update { it.copy(selectedAudioUrl = action.audioUrl) }
            }
        }
    }

}
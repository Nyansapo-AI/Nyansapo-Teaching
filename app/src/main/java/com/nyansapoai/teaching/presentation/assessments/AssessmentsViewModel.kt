package com.nyansapoai.teaching.presentation.assessments

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AssessmentsViewModel(
    private val assessmentsRepository: AssessmentRepository,
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(AssessmentsState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                fetchAssessments()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AssessmentsState()
        )

    fun onAction(action: AssessmentsAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

    private fun fetchAssessments() {
        viewModelScope.launch {
            _state.update { it.copy(assessmentListState = Results.loading()) }
            assessmentsRepository.getAssessments()
                .catch { e ->
                    Log.e("AssessmentsViewModel", "Error fetching assessments: ${e.message}")
                    _state.value = _state.value.copy(
                        assessmentListState = Results.error(msg = e.message ?: "Something went wrong, try again later.")
                    )
                }
                .collect{ assessments->
                    Log.d("AssessmentsViewModel", "Fetched assessments: ${assessments}")
                    _state.value = _state.value.copy(
                        assessmentListState = Results.success(data = assessments)
                    )
                }
        }
    }

}
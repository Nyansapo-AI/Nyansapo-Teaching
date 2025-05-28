package com.nyansapoai.teaching.presentation.assessments.IndividualAssessment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IndividualAssessmentViewModel(
    private val assessmentRepository: AssessmentRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(IndividualAssessmentState())
    val state = _state.asStateFlow()
        /*
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = IndividualAssessmentState()
        )
         */
    fun onAction(action: IndividualAssessmentAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }


    fun getAssessmentById(assessmentId: String) {
        viewModelScope.launch {
            _state.update { it.copy(assessmentState = Results.loading()) }
            assessmentRepository.getAssessmentById(assessmentId)
                .catch { e ->
                    _state.update { it.copy(assessmentState = Results.error(msg = e.message ?: "Something went wrong!")) }
                }
                .collect{ assessment ->
                    Log.d("IndividualAssessmentViewModel", "Fetched assessment: $assessment")
//                    _state.update { it.copy(assessmentState = assessment) }
                    _state.value = _state.value.copy(
                        assessmentState = assessment
                    )
                }
        }
    }

}
package com.nyansapoai.teaching.presentation.assessments.assessmentResult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class AssessmentResultViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(AssessmentResultState())
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
            initialValue = AssessmentResultState()
        )

    fun onAction(action: AssessmentResultAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}
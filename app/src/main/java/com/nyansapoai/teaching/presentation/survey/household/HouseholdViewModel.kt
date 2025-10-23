package com.nyansapoai.teaching.presentation.survey.household

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.remote.survey.SurveyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class HouseholdViewModel(
    private val surveyRepository: SurveyRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(HouseholdState())
    val state = combine(
        _state,
        surveyRepository.getHouseholdSurveys()

    ){ states, households ->
        states.copy(isLoading = true)

        states.copy(
            households = households,
            isLoading = false,
            errorMessage = null
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
            initialValue = HouseholdState()
        )

    fun onAction(action: HouseholdAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}
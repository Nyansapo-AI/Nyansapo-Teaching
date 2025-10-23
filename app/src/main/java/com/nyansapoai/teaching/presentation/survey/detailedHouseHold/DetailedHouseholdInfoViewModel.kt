package com.nyansapoai.teaching.presentation.survey.detailedHouseHold

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.nyansapoai.teaching.data.remote.survey.SurveyRepository
import com.nyansapoai.teaching.navigation.HouseHoldDetailsPage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class   DetailedHouseholdInfoViewModel(
    savedStateHandle: SavedStateHandle,
    private val surveyRepository: SurveyRepository
) : ViewModel() {
    private val household = savedStateHandle.toRoute<HouseHoldDetailsPage>()

    private val _state = MutableStateFlow(DetailedHouseholdInfoState())
    val state = combine(
        _state,
        surveyRepository.getHouseholdSurveyById(id = household.houseHoldId)
    ){ currentState, householdInfo ->

        currentState.copy(isLoading = true)
        currentState.copy(
            householdInfo = householdInfo,
            isLoading = false
        )

    }
        .onStart {

        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = DetailedHouseholdInfoState()
        )

    fun onAction(action: DetailedHouseholdInfoAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}
package com.nyansapoai.teaching.presentation.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class OnboardingViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(OnboardingState())
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
            initialValue = OnboardingState()
        )

    fun onAction(action: OnboardingAction) {
        when (action) {
            is OnboardingAction.OnStepChange -> {

                _state.update { it.copy(currentStep = action.step) }
            }

            is OnboardingAction.OnSelectOrganization -> {
                _state.update { it.copy(selectedOrganization = action.organizationUI) }
            }

            is OnboardingAction.OnSelectCamp -> {
                _state.update { it.copy(selectedCamp = action.camp) }
            }
            is OnboardingAction.OnSelectProject -> {
                _state.update { it.copy(selectedProject = action.project) }
            }
            is OnboardingAction.OnSelectSchool -> {
                _state.update { it.copy(selectedSchool = action.school) }
            }
        }
    }

}
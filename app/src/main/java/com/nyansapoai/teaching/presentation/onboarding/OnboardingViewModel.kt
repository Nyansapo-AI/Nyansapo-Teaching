package com.nyansapoai.teaching.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.remote.user.UserRepository
import com.nyansapoai.teaching.utils.ResultStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val userRepository: UserRepository,
    private val localDataSource: LocalDataSource
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(OnboardingState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/

                fetchUserData()
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
                _state.update { it.copy(selectedOrganization = action.organizationUI, currentStep = it.currentStep + 1) }
            }

            is OnboardingAction.OnSelectCamp -> {
                _state.update { it.copy(selectedCamp = action.camp, currentStep = it.currentStep + 1) }
            }
            is OnboardingAction.OnSelectProject -> {
                _state.update { it.copy(selectedProject = action.project, currentStep = it.currentStep + 1) }
            }
            is OnboardingAction.OnSelectSchool -> {
                _state.update { it.copy(selectedSchool = action.school, currentStep = it.currentStep + 1) }
            }

            is OnboardingAction.OnContinue -> {
                saveInfo(
                    onSuccess = { action.onSuccess.invoke() }
                )
            }
        }
    }


    private fun fetchUserData(){
        viewModelScope.launch(Dispatchers.IO) {

            val userData = userRepository.getUserDetails()

            when(userData.status){
                ResultStatus.INITIAL,
                ResultStatus.LOADING -> {
                    _state.update { it.copy(isLoading = true) }

                }
                ResultStatus.SUCCESS -> {
                    _state.update { it.copy(userData = userData.data ) }
                }
                ResultStatus.ERROR -> {
                    _state.update {
                        it.copy(error = userData.message)
                    }
                }
            }
        }
    }

    private fun saveInfo(onSuccess: () -> Unit){
        if (_state.value.selectedOrganization == null || _state.value.selectedProject == null || _state.value.selectedSchool == null){
            _state.update {
                it.copy(error = "Please select the fields required")
            }
            return
        }


        viewModelScope.launch(Dispatchers.IO) {

            localDataSource.saveCurrentSchoolInfo(
                organizationUid = _state.value.selectedOrganization?.id ?: "",
                projectUid = _state.value.selectedProject?.id ?: "",
                schoolUid = _state.value.selectedSchool?.id ?: ""
            )
        }
    }

}
package com.nyansapoai.teaching.presentation.survey.household

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.remote.survey.SurveyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HouseholdViewModel(
    private val surveyRepository: SurveyRepository,
    private val localDataSource: LocalDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(HouseholdState())
    val state = combine(
        _state,
        localDataSource.getSavedCurrentSchoolInfo(),
    ){ states, schoolInfo ->
        states.copy(
            isLoading = true,
        )

        fetchHouseholds(
            organizationId = schoolInfo.organizationUid,
            projectId = schoolInfo.projectUId,
            schoolId = schoolInfo.schoolUId
        )

        states.copy(
            localSchoolInfo = schoolInfo
        )
    }
        .onStart {
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

    private fun fetchHouseholds(organizationId: String, projectId: String, schoolId: String) {
        if (organizationId.isEmpty() || projectId.isEmpty() || schoolId.isEmpty()) {
            return
        }
        viewModelScope.launch {
            surveyRepository.getHouseholdSurveys(organizationId, projectId, schoolId)
                .collect { households ->
                    _state.value = _state.value.copy(households = households)
                }
        }
    }

}
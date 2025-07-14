package com.nyansapoai.teaching.presentation.schools

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.remote.school.SchoolRepository
import com.nyansapoai.teaching.data.remote.user.UserRepository
import com.nyansapoai.teaching.utils.ResultStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class SchoolViewModel(
    private val userRepository: UserRepository,
    private val schoolRepository: SchoolRepository,
    private val localDataSource: LocalDataSource
) : ViewModel() {

    private val TAG = "SchoolViewModel"

    private val _state = MutableStateFlow(SchoolState())
    val state = combine(
        _state,
        userRepository.getUserDetails(),
        localDataSource.getSavedCurrentSchoolInfo()
    ) { currentState, user, localSchoolInfo->
        currentState.copy(
            user = user.data,
            localSchoolInfo = localSchoolInfo,
            isLoading = false
        )
    }.onStart {
        _state.update { it.copy(isLoading = true) }
        _state.update {
            it.copy(greeting = getTimeBasedGreeting())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = SchoolState()
    )

    fun onAction(action: SchoolAction) {
        when (action) {
            is SchoolAction.OnShowSchoolSelector -> {
                _state.update {
                    it.copy(
                        showSchoolSelector = action.show
                    )
                }
            }

            is SchoolAction.OnFetchSchoolDetails -> {
                fetchSchoolDetails(
                    organizationId = action.organizationId,
                    projectId = action.projectId,
                    schoolId = action.schoolId
                )
            }
            is SchoolAction.OnSelectSchool -> {}
        }
    }

    private fun getTimeBasedGreeting(): String {
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val hourOfDay = currentDateTime.hour

        return when {
            hourOfDay < 12 -> "Good Morning"
            hourOfDay < 17 -> "Good Afternoon"
            hourOfDay < 21 -> "Good Evening"
            else -> "Good Night"
        }
    }

    private fun fetchCurrentUserDetails() {
        viewModelScope.launch {
            val userData = userRepository.getUserDetails().first()

            Log.d(TAG, "User information: $userData")

            when (userData.status) {
                ResultStatus.INITIAL,
                ResultStatus.LOADING -> {
                    _state.update { it.copy(isLoading = true) }
                }
                ResultStatus.SUCCESS -> {
                    _state.update {
                        it.copy(
                            user = userData.data,
                            isLoading = false
                        )
                    }
                }
                ResultStatus.ERROR -> {
                    _state.update {
                        it.copy(error = userData.message, isLoading = false)
                    }
                }
            }
        }
    }

    fun fetchSchoolDetails(organizationId: String, projectId: String, schoolId: String) {
        if (organizationId.isEmpty() || projectId.isEmpty() || schoolId.isEmpty()) {
            Log.w(TAG, "Invalid IDs: org=$organizationId, project=$projectId, school=$schoolId")
            _state.update { it.copy(error = "Invalid school identifiers", isLoading = false) }
            return
        }

        Log.d(TAG, "Fetching school details")
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val schoolData = schoolRepository.getSchoolInfo(
                organizationId = organizationId,
                projectId = projectId,
                schoolId = schoolId
            ).first()

            when(schoolData.status){
                ResultStatus.INITIAL ,
                ResultStatus.LOADING -> {
                    _state.update {  it.copy(isLoading = true)}
                }
                ResultStatus.SUCCESS -> {
                    _state.update {
                        it.copy(
                            schoolDetails = schoolData.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                ResultStatus.ERROR -> {
                    _state.update {
                        it.copy(
                            error = schoolData.message ?: "Failed to load school details",
                            isLoading = false
                        )
                    }
                }
            }

            /*
            schoolRepository.getSchoolInfo(
                organizationId = organizationId,
                projectId = projectId,
                schoolId = schoolId
            )
                .catch { e ->
                    Log.e(TAG, "Error fetching school details: ${e.message}")
                    _state.update { it.copy(error = e.message, isLoading = false) }
                }
                .collect { data ->
                    if (data.data != null) {
                        _state.update {
                            it.copy(
                                schoolDetails = data.data,
                                isLoading = false,
                                error = null
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                error = data.message ?: "Failed to load school details",
                                isLoading = false
                            )
                        }
                    }
                }
            */
        }
    }
}
package com.nyansapoai.teaching.presentation.attendances

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.remote.attendance.AttendanceRepository
import com.nyansapoai.teaching.domain.models.school.LocalSchoolInfo
import com.nyansapoai.teaching.utils.ResultStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AttendancesViewModel(
    private val localDataSource: LocalDataSource,
    private val attendancesRepository: AttendanceRepository,
) : ViewModel() {


    private val _state = MutableStateFlow(AttendancesState())

    private var lastRequestedKey: Pair<String, String>? = null

    val state = combine(
        _state,
        localDataSource.getSavedCurrentSchoolInfo()
    ){currentState, localSchoolInfo ->
        currentState.copy(
            localSchoolInfo = localSchoolInfo,
        )
    }.onStart {

    }
        .onEach { combineState ->
            val date = combineState.currentWeekDay ?: return@onEach

            val localInfo = combineState.localSchoolInfo ?: return@onEach

            val key = date to localInfo.schoolUId
            if (key == lastRequestedKey) {
                // already requested for this date + school
                return@onEach
            }
            lastRequestedKey = key

            getAttendance(
                date = date,
                localSchoolInfo = localInfo
            )


        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = _state.value
        )

    fun onAction(action: AttendancesAction) {
        when (action) {
            is AttendancesAction.SetWeekDay -> {
                _state.value = _state.value.copy(
                    currentWeekDay = action.weekDay.date.toString()
                )

            }

            is AttendancesAction.SetShowDetailedAttendance -> {
                _state.update {
                    it.copy(
                        showDetailedAttendance = action.showDetailAttendance
                    )
                }
            }
        }
    }

    private fun getAttendance(date: String, localSchoolInfo: LocalSchoolInfo?){
        if (localSchoolInfo == null) {
            _state.update {
                it.copy(
                    errorMessage = "Local school info is missing",
                    isLoading = false
                )
            }
            Log.e("AttendancesViewModel", "getAttendance: Local school info is missing")
            return
        }


        viewModelScope.launch(Dispatchers.IO) {
            attendancesRepository.getAttendanceDataByDate(
                date = date,
                organizationId = localSchoolInfo.organizationUid,
                schoolId = localSchoolInfo.schoolUId,
                projectId = localSchoolInfo.projectUId,
            )
                .onStart {_state.update { it.copy(isLoading = true) }}
                .catch { error ->  _state.update { it.copy(errorMessage = error.message, isLoading = false) } }
                .collect { response ->
                    when(response.status){
                        ResultStatus.INITIAL ,
                        ResultStatus.LOADING -> {
                            _state.update { it.copy(isLoading = true) }
                        }
                        ResultStatus.SUCCESS -> {
                            _state.update { it.copy(isLoading = false, attendanceRecord = response.data) }
                        }
                        ResultStatus.ERROR -> {
                            _state.update { it.copy(errorMessage = response.message, isLoading = false) }
                        }
                    }

                }


        }
    }

}
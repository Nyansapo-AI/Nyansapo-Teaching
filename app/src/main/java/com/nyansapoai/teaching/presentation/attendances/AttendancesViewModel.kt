package com.nyansapoai.teaching.presentation.attendances

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.remote.attendance.AttendanceRepository
import com.nyansapoai.teaching.domain.models.school.LocalSchoolInfo
import com.nyansapoai.teaching.presentation.students.StudentsState
import com.nyansapoai.teaching.utils.ResultStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AttendancesViewModel(
    private val localDataSource: LocalDataSource,
    private val attendancesRepository: AttendanceRepository,
) : ViewModel() {


    private val _state = MutableStateFlow(AttendancesState())

    val state = combine(
        _state,
        localDataSource.getSavedCurrentSchoolInfo()
    ){currentState, localSchoolInfo ->
        currentState.copy(
            localSchoolInfo = localSchoolInfo,
        )
        /*
        currentState.currentWeekDay?.let {
            getAttendance(
                date = it,
                localSchoolInfo = localSchoolInfo
            )
        }*/
    }.onStart {
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

                _state.value.currentWeekDay?.let { date ->
                    val localInfo = state.value.localSchoolInfo
                    getAttendance(
                        date = date,
                        localSchoolInfo = localInfo
                    )

                }

            }
        }
    }

    private fun getAttendance(date: String, localSchoolInfo: LocalSchoolInfo?){
        viewModelScope.launch(Dispatchers.IO) {
            val response = attendancesRepository.getAttendanceData(
                date = date,
                organizationId = localSchoolInfo?.organizationUid ?: "",
                schoolId = localSchoolInfo?.schoolUId ?: "",
                projectId = localSchoolInfo?.schoolUId ?: "",
            )

            Log.d("ATTENDANCE", "getAttendance: $response")

            when(response.status) {
                ResultStatus.INITIAL ,
                ResultStatus.LOADING -> {
                    _state.update { it.copy(isLoading = true) }
                }
                ResultStatus.SUCCESS -> {
                    _state.update {
                        it.copy(attendanceRecord = response.data)
                    }
                }
                ResultStatus.ERROR -> {
                    _state.update {
                        it.copy(
                            errorMessage = response.message,
                            isLoading = false
                        )
                    }
                }
            }


        }
    }

}
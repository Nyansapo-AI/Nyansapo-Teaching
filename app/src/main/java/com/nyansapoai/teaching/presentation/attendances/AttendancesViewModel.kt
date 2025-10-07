package com.nyansapoai.teaching.presentation.attendances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.presentation.students.StudentsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class AttendancesViewModel(
    private val localDataSource: LocalDataSource,
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(AttendancesState())

    val state = combine(
        _state,
        localDataSource.getSavedCurrentSchoolInfo()
    ){currentState, localSchoolInfo ->
        currentState.copy(
            localSchoolInfo = localSchoolInfo,
        )
    }.onStart {
        if (!hasLoadedInitialData) {
            /** Load initial data here **/
            hasLoadedInitialData = true
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AttendancesState()
        )

    fun onAction(action: AttendancesAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}
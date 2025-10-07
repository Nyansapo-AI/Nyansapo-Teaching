package com.nyansapoai.teaching.presentation.attendances.collectAttendance

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.nyansapoai.teaching.data.remote.attendance.AttendanceRepository
import com.nyansapoai.teaching.data.remote.students.StudentsRepository
import com.nyansapoai.teaching.domain.models.attendance.AttendanceRecord
import com.nyansapoai.teaching.domain.models.attendance.StudentAttendance
import com.nyansapoai.teaching.navigation.CollectAttendancePage
import com.nyansapoai.teaching.utils.ResultStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectAttendanceViewModel(
    savedStateHandle: SavedStateHandle,
    private val attendancesRepository: AttendanceRepository,
    private val studentsRepository: StudentsRepository,
) : ViewModel() {

    private val collectAttendanceRouteArgs = savedStateHandle.toRoute<CollectAttendancePage>()


    private val _state = MutableStateFlow(CollectAttendanceState())
    val state = _state
        .onStart {
            fetchSchoolDetails(
                organizationId = collectAttendanceRouteArgs.organizationId,
                projectId = collectAttendanceRouteArgs.projectId,
                schoolId = collectAttendanceRouteArgs.schoolId,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CollectAttendanceState()
        )

    fun onAction(action: CollectAttendanceAction) {
        when (action) {
            is CollectAttendanceAction.OnMarkAttendance -> {
                _state.update { state ->
                    val index = state.studentAttendanceList.indexOfFirst { it.id == action.studentId }

                    if (index != -1) {
                        val newList = state.studentAttendanceList.toMutableList()
                        newList[index] = newList[index].copy(attendance = action.isPresent)
                        state.copy(studentAttendanceList = newList)
                    } else {
                        state
                    }
                }
            }

            is CollectAttendanceAction.OnSelectedGrade -> {
                    _state.update { state ->
                    // Filter students by the selected grade or show all if grade is null
                    val filteredStudents = if (action.grade != null) {
                        state.studentList.filter { it.grade == action.grade }
                    } else {
                        state.studentList
                    }

                    // Create attendance records from filtered students
                    val attendanceList = filteredStudents.map { student ->
                        StudentAttendance(
                            id = student.id,
                            name = student.name,
                            grade = student.grade,
                            attendance = false  // Default to not present
                        )
                    }.toMutableList()

                    state.copy(
                        studentAttendanceList = attendanceList,
                        selectedGrade = action.grade
                    )
                }
            }
            is CollectAttendanceAction.OnSubmitAttendance -> TODO()
        }
    }


    fun fetchSchoolDetails(organizationId: String, projectId: String, schoolId: String, grade: Int? = null) {
        if (organizationId.isEmpty() || projectId.isEmpty() || schoolId.isEmpty()) {
            _state.update { it.copy(error = "Invalid school identifiers", isLoading = false) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val schoolData = studentsRepository.getSchoolStudents(
                organizationId = organizationId,
                projectId = projectId,
                schoolId = schoolId,
                studentClass = grade
            ).first()

            when(schoolData.status){
                ResultStatus.INITIAL ,
                ResultStatus.LOADING -> {
                    _state.update {  it.copy(isLoading = true)}
                }
                ResultStatus.SUCCESS -> {

                    Log.d("Collect Attendance", "Fetched school students: ${schoolData.data?.size ?: 0} students")
                    _state.update {
                        it.copy(
                            studentList = schoolData.data ?: emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }
                }
                ResultStatus.ERROR -> {
                    Log.w("Collect Attendance", "Error fetching school students: ${schoolData.message}")
                    _state.update {
                        it.copy(
                            error = schoolData.message ?: "Failed to load school details",
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun submitAttendance(
        organizationId: String,
        projectId: String,
        schoolId: String,
        attendanceList: List<StudentAttendance>
    ) {
        if (organizationId.isEmpty() || projectId.isEmpty() || schoolId.isEmpty()) {
            _state.update { it.copy(error = "Invalid school identifiers", isLoading = false) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val response = attendancesRepository.submitAttendanceData(
                attendanceRecord = AttendanceRecord(
                )
            )

            when(response.status){
                ResultStatus.INITIAL ,
                ResultStatus.LOADING -> {
                    _state.update {  it.copy(isLoading = true)}
                }
                ResultStatus.SUCCESS -> {
                    Log.d("Collect Attendance", "Successfully submitted attendance")
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null
                        )
                    }
                }
                ResultStatus.ERROR -> {
                    Log.w("Collect Attendance", "Error submitting attendance: ${response.message}")
                    _state.update {
                        it.copy(
                            error = response.message ?: "Failed to submit attendance",
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

}
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
            Log.d("Attendance Data", "attendance collect attendance viewmodel school info: $collectAttendanceRouteArgs.")
            Log.d("Attendance Data Date", "attendance collect attendance viewmodel school info: ${collectAttendanceRouteArgs.date}.")


            fetchSchoolDetails(
                organizationId = collectAttendanceRouteArgs.organizationId,
                projectId = collectAttendanceRouteArgs.projectId,
                schoolId = collectAttendanceRouteArgs.schoolId,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = _state.value
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
                        state.studentAttendanceList.filter { it.grade == action.grade }.toMutableList()
                    } else {
                        state.studentAttendanceList
                    }


                        Log.d("Collect Attendance", "Selected grade: ${state.studentAttendanceList}")
                        Log.d("Collect Attendance", "Selected grade: ${state.studentList}")


                    state.copy(
                        studentAttendanceList = filteredStudents,
                        selectedGrade = action.grade
                    )
                }
            }
            is CollectAttendanceAction.OnSubmitAttendance -> {
                submitAttendance(
                    organizationId = collectAttendanceRouteArgs.organizationId,
                    projectId = collectAttendanceRouteArgs.projectId,
                    schoolId = collectAttendanceRouteArgs.schoolId,
                    date = collectAttendanceRouteArgs.date,
                    attendanceList = state.value.studentAttendanceList,
                    onSuccess = {action.onSuccess}
                )
            }
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
//                            studentList = schoolData.data ?: emptyList(),
                            studentAttendanceList = schoolData.data?.map { student ->
                                StudentAttendance(
                                    id = student.id,
                                    name = student.first_name + " " + student.last_name,
                                    grade = student.grade,
                                    attendance = false
                                )
                            }?.toMutableList() ?: mutableListOf(),
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
        date: String,
        attendanceList: List<StudentAttendance>,
        onSuccess: () -> Unit = { }
    ) {
        if (organizationId.isEmpty() || projectId.isEmpty() || schoolId.isEmpty()) {
            _state.update { it.copy(error = "Invalid school identifiers", isLoading = false) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val response = attendancesRepository.submitAttendanceData(
                organizationId = organizationId,
                projectId = projectId,
                schoolId = schoolId,
                attendanceRecord = AttendanceRecord(
                    date = date,
                    students = attendanceList
                ),
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

                    onSuccess.invoke()
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
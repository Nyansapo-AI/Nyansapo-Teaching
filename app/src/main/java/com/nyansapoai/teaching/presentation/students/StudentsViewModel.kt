package com.nyansapoai.teaching.presentation.students

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.remote.students.StudentsRepository
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

class StudentsViewModel(
    private val studentsRepository: StudentsRepository,
    private val localDataSource: LocalDataSource,
    private val userRepository: UserRepository
) : ViewModel() {

    private val TAG = "StudentsViewModel"
    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(StudentsState())
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
            initialValue = StudentsState()
        )

    fun onAction(action: StudentsAction) {
        when (action) {
            is StudentsAction.OnFetchStudents -> {
                fetchSchoolDetails(
                    organizationId = action.organizationId,
                    projectId = action.projectId,
                    schoolId = action.schoolId,
                    grade = action.grade
                )
            }

            is StudentsAction.OnSelectGrade -> {
                _state.update { it.copy(selectedGrade = action.grade) }
            }

        }
    }

    fun fetchSchoolDetails(organizationId: String, projectId: String, schoolId: String, grade: Int? = null) {
        if (organizationId.isEmpty() || projectId.isEmpty() || schoolId.isEmpty()) {
            Log.w(TAG, "Invalid IDs: org=$organizationId, project=$projectId, school=$schoolId")
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

                    Log.d(TAG, "Fetched school students: ${schoolData.data?.size ?: 0} students")
                    _state.update {
                        it.copy(
                            studentList = schoolData.data ?: emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }
                }
                ResultStatus.ERROR -> {
                    Log.w(TAG, "Error fetching school students: ${schoolData.message}")
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


}
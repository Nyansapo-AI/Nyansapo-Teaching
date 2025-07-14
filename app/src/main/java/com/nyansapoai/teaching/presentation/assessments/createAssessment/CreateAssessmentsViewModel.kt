package com.nyansapoai.teaching.presentation.assessments.createAssessment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.data.remote.students.StudentsRepository
import com.nyansapoai.teaching.data.remote.user.UserRepository
import com.nyansapoai.teaching.domain.models.students.NyansapoStudent
import com.nyansapoai.teaching.presentation.common.snackbar.SnackBarHandler
import com.nyansapoai.teaching.presentation.common.snackbar.SnackBarItem
import com.nyansapoai.teaching.utils.ResultStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateAssessmentsViewModel(
    private val assessmentRepository: AssessmentRepository,
    private val snackBarHandler: SnackBarHandler,
    private val studentsRepository: StudentsRepository,
    private val localDataSource: LocalDataSource,
    private val userRepository: UserRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val TAG = "CreateAssessmentsViewModel"

    private val _state = MutableStateFlow(CreateAssessmentsState())

    val state = combine(
        _state,
        localDataSource.getSavedCurrentSchoolInfo(),
        userRepository.getUserDetails()
    ){currentState , schoolInfo , user->
        currentState.copy(
            localSchoolInfo = schoolInfo,
            isManager = true
        )
    }
        .onStart {
            _state.update { it.copy(isLoading = true) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CreateAssessmentsState()
        )

    fun onAction(action: CreateAssessmentsAction) {
        when (action) {
            is CreateAssessmentsAction.AddAssignedStudent -> {

                _state.update { currentState ->
                    val isDuplicate = currentState.assignedStudents.any { it == action.student }
                    if (!isDuplicate) {
                        currentState.copy(assignedStudents = currentState.assignedStudents + action.student)
                    } else {
                        currentState.copy(assignedStudents = currentState.assignedStudents - action.student)
                    }
                }
            }
            is CreateAssessmentsAction.SetAssessmentNumber -> {
                _state.update { it.copy(assessmentNumber = action.assessmentNumber) }
            }
            is CreateAssessmentsAction.SetName -> {
                _state.update { it.copy(name = action.name) }
            }
            is CreateAssessmentsAction.SetStartLevel -> {
                _state.update { it.copy(startLevel = action.startLevel, isStartLevelDropDownExpanded = false) }
            }
            is CreateAssessmentsAction.SetType -> {
                _state.update {
                    it.copy(
                        type = action.type,
                        isTypeDropDownExpanded = false,
                    )
                }
            }

            is CreateAssessmentsAction.ToggleAssessmentNumberDropDown -> {
                _state.update { it.copy(isAssessmentNumberDropDownExpanded = action.isExpanded) }
            }
            is CreateAssessmentsAction.ToggleStartLevelDropDown -> {
                _state.update { it.copy(isStartLevelDropDownExpanded = action.isExpanded) }
            }
            is CreateAssessmentsAction.ToggleTypeDropDown -> {
                _state.update { it.copy(isTypeDropDownExpanded = action.isExpanded) }
            }

            is CreateAssessmentsAction.ToggleStudentListDropDown -> {
                _state.update { it.copy(isStudentListDropDownExpanded = action.isExpanded) }
            }

            is CreateAssessmentsAction.SubmitAssessment -> {
                createAssessment(
                    name = _state.value.name,
                    type = _state.value.type,
                    startLevel = _state.value.startLevel,
                    assessmentNumber = _state.value.assessmentNumber,
                    assignedStudents = _state.value.assignedStudents
                )
            }

            is CreateAssessmentsAction.OnFetchStudents -> {
                fetchSchoolDetails(
                    organizationId = action.organizationId,
                    projectId = action.projectId,
                    schoolId = action.schoolId,
                    grade = action.grade
                )
            }

            is CreateAssessmentsAction.SetSelectedGrade -> {
                _state.update {
                    it.copy(selectedGrade = action.grade)
                }
            }

        }
    }

    private fun createAssessment(
        name: String,
        type: String,
        startLevel: String,
        assessmentNumber: Int,
        assignedStudents: List<NyansapoStudent>
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val result = assessmentRepository.createAssessment(
                name = name,
                type = type,
                startLevel = startLevel,
                assessmentNumber = assessmentNumber,
                assignedStudents = assignedStudents
            )

            when (result.status){
                ResultStatus.INITIAL ,
                ResultStatus.LOADING -> {
                    _state.update { it.copy(isLoading = true) }
                }
                ResultStatus.SUCCESS -> {

                    _state.update {
                        it.copy(
                            name = "",
                            type = "",
                            startLevel = "",
                            assessmentNumber = 1,
                            assignedStudents = emptyList(),
                            isAssessmentNumberDropDownExpanded = false,
                            isStartLevelDropDownExpanded = false,
                            isTypeDropDownExpanded = false,
                            isStudentListDropDownExpanded = false,
                            isLoading = false
                        )
                    }


                    snackBarHandler.showSnackBarNotification(
                        snackBarItem = SnackBarItem(
                            message = "Assessment created successfully",
                            isError = false
                        )
                    )

                }
                ResultStatus.ERROR -> {
                    snackBarHandler.showSnackBarNotification(
                        snackBarItem = SnackBarItem(
                            message = result.message ?: "Failed to create assessment",
                            isError = true
                        )
                    )
                }
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



    init {
        /*
        createAssessment(
            name = "Sample Assessment",
            type = "Quiz",
            startLevel = "Beginner",
            assessmentNumber = 1,
            assignedStudents = listOf("student1", "student2") // Example student IDs
        )*/
    }

}
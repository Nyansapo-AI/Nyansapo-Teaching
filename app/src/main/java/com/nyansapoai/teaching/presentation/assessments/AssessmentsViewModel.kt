package com.nyansapoai.teaching.presentation.assessments

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AssessmentsViewModel(
    private val assessmentsRepository: AssessmentRepository,
    private val localDataSource: LocalDataSource
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(AssessmentsState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                getLocalSchoolInfo()

//                fetchAssessments()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AssessmentsState()
        )

    fun onAction(action: AssessmentsAction) {
        when (action) {
            is AssessmentsAction.OnGetCompletedAssessments -> {

            }

            is AssessmentsAction.FetchAssessments -> {
                fetchAssessments(schoolId = action.schoolId)
            }
        }
    }


    private fun getLocalSchoolInfo() {
        viewModelScope.launch {
            localDataSource.getSavedCurrentSchoolInfo()
                .catch { e ->
                    Log.e("AssessmentsViewModel", "Error fetching local school info: ${e.message}")
                }
                .collect { localSchoolInfo ->
                    Log.d("AssessmentsViewModel", "Fetched local school info: ${localSchoolInfo}")
                    _state.value = _state.value.copy(localSchoolInfo = localSchoolInfo)
                }
        }
    }

    private fun fetchAssessments(schoolId: String = "") {
        viewModelScope.launch {
            _state.update { it.copy(assessmentListState = Results.loading()) }
            assessmentsRepository.getAssessments(schoolId = schoolId)
                .catch { e ->
                    Log.e("AssessmentsViewModel", "Error fetching assessments: ${e.message}")
                    _state.value = _state.value.copy(
                        assessmentListState = Results.error(msg = e.message ?: "Something went wrong, try again later.")
                    )
                }
                .collect{ assessments->
                    Log.d("AssessmentsViewModel", "Fetched assessments: ${assessments}")
                    _state.value = _state.value.copy(
                        assessmentListState = Results.success(data = assessments)
                    )
                }
        }
    }


}
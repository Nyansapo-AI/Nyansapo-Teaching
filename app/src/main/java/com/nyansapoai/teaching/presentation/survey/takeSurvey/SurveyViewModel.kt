package com.nyansapoai.teaching.presentation.survey.takeSurvey

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.remote.students.StudentsRepository
import com.nyansapoai.teaching.domain.models.school.LocalSchoolInfo
import com.nyansapoai.teaching.domain.models.survey.Child
import com.nyansapoai.teaching.domain.models.survey.CreateHouseHoldInfo
import com.nyansapoai.teaching.domain.models.survey.Parent
import com.nyansapoai.teaching.presentation.common.connectivity.NetworkConnectivityObserver
import com.nyansapoai.teaching.presentation.survey.takeSurvey.SurveyState.Companion.toCreateHouseHoldInfo
import com.nyansapoai.teaching.presentation.survey.workers.SubmitHouseholdSurveyWorker
import com.nyansapoai.teaching.utils.ResultStatus
import com.nyansapoai.teaching.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SurveyViewModel(
    private val localDataSource: LocalDataSource,
    private val studentsRepository: StudentsRepository,
    private val workManager: WorkManager,
    private val networkObserver: NetworkConnectivityObserver
) : ViewModel() {

    val isOnline: StateFlow<Boolean> = networkObserver.observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val _state = MutableStateFlow(SurveyState())
    val state = combine(
        _state,
        localDataSource.getSavedCurrentSchoolInfo(),
        localDataSource.getChildrenInPendingHouseholds()
    ){ currentState, localSchoolInfo, linkedLearners ->
        val linked = linkedLearners.map { it.linkedLearnerId }

        currentState.copy(
            localSchoolInfo = localSchoolInfo,
            isLinkedIdList = linked.toMutableList()
        )

    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = _state.value
        )

    fun onAction(action: SurveyAction) {
        when (action) {
            is SurveyAction.SetConsentGiven -> {
                _state.update { it.copy(consentGiven = action.given) }
            }

            is SurveyAction.SetCounty -> {
                _state.update {
                    it.copy(
                        county = action.county,
                        subCounty = ""
                    )
                }
            }

            is SurveyAction.SetInterviewerName -> {
                _state.update { it.copy(interviewerName = action.name) }
            }

            is SurveyAction.SetSubCounty -> {
                _state.update { it.copy(subCounty = action.subCounty) }
            }

            is SurveyAction.SetWard -> {
                _state.update { it.copy(ward = action.ward) }
            }

            is SurveyAction.SetHouseholdHeadMobileNumber -> {
                _state.update {
                    it.copy(
                        householdHeadMobileNumber = action.mobileNumber,
                        mobileNumberError = if (Utils.isValidPhoneNumber(phone = action.mobileNumber)) null else "Invalid mobile number"
                    )
                }
            }

            is SurveyAction.SetHouseholdHeadName -> {
                _state.update {
                    it.copy(householdHeadName = action.name)
                }
            }

            is SurveyAction.SetIsRespondentHeadOfHousehold -> {
                _state.update {
                    it.copy(
                        isRespondentHeadOfHousehold = action.isHead,
                        householdHeadName = if (action.isHead) it.respondentName else it.householdHeadName
                    )
                }
            }

            is SurveyAction.SetMainLanguageSpokenAtHome -> {
                _state.update { it.copy(mainLanguageSpokenAtHome = action.language) }
            }

            is SurveyAction.SetRelationshipToHead -> {
                _state.update {
                    it.copy(relationshipToHead = action.relationship)
                }
            }

            is SurveyAction.SetRespondentAge -> {
                _state.update { it.copy(respondentAge = action.age) }
            }

            is SurveyAction.SetRespondentName -> {
                _state.update {
                    it.copy(
                        respondentName = action.name,
                        householdHeadName = if (it.isRespondentHeadOfHousehold) action.name else it.householdHeadName
                    )
                }
            }

            is SurveyAction.SetShowMainLanguageDropdown -> {
                _state.update { it.copy(showMainLanguageDropdown = action.show) }
            }

            is SurveyAction.SetShowRelationshipToHeadDropdown -> {
                _state.update { it.copy(showRelationshipToHeadDropdown = action.show) }
            }

            is SurveyAction.SetTotalHouseholdMembers -> {
                _state.update { it.copy(totalHouseholdMembers = action.total) }
            }

            is SurveyAction.SetHouseholdIncomeSource -> {
                _state.update { it.copy(houseHoldIncomeSource = action.source) }
            }

            is SurveyAction.SetShowIncomeSourceDropdown -> {
                _state.update { it.copy(showIncomeSourceDropdown = action.show) }
            }

            is SurveyAction.SetHasElectricity -> {
                _state.update { it.copy(hasElectricity = action.hasElectricity) }
            }

            is SurveyAction.SetHouseholdAssets -> {
                _state.update { it.copy(householdAssets = action.assets.toMutableList()) }
            }

            is SurveyAction.SetShowAssetsDropdown -> {
                _state.update { it.copy(showAssetsDropdown = action.show) }
            }

            is SurveyAction.SetDiscussFrequency -> {
                _state.update { it.copy(discussFrequency = action.frequency) }
            }

            is SurveyAction.SetDoAttendMeetings -> {
                _state.update { it.copy(doAttendMeetings = action.attend) }
            }

            is SurveyAction.SetDoMonitorAttendance -> {
                _state.update { it.copy(doMonitorAttendance = action.monitor) }
            }

            is SurveyAction.SetIsSchoolAgeChildrenPresent -> {
                _state.update { it.copy(isSchoolAgeChildrenPresent = action.isPresent) }
            }

            is SurveyAction.SetShowDiscussWithTeachersDropdown -> {
                _state.update { it.copy(showDiscussWithTeachersDropdown = action.show) }
            }

            is SurveyAction.SetShowWhoHelpsDropdown -> {
                _state.update { it.copy(showWhoHelpsDropdown = action.show) }
            }

            is SurveyAction.SetWhoHelps -> {
                _state.update {
                    it.copy(
                        whoHelps = action.whoHelps,
                        otherWhoHelps = if (action.whoHelps.contains("Other")) "" else it.otherWhoHelps
                    )
                }
            }

            is SurveyAction.SetOtherWhoHelps -> {
                _state.update { it.copy(otherWhoHelps = action.other) }
            }

            is SurveyAction.SetHasLearningMaterials -> {
                _state.update { it.copy(hasLearningMaterials = action.hasMaterials) }
            }

            is SurveyAction.SetHasMissedSchool -> {
                _state.update { it.copy(hasMissedSchool = action.hasMissed) }
            }

            is SurveyAction.SetHasQuietPlaceAvailable -> {
                _state.update { it.copy(isQuietPlaceAvailable = action.isAvailable) }
            }

            is SurveyAction.SetMissedReason -> {
                _state.update { it.copy(missedReason = action.reason) }
            }

            is SurveyAction.SetOtherMissedReason -> {
                _state.update { it.copy(otherMissedReason = action.other) }
            }

            is SurveyAction.SetShowMissedReasonDropdown -> {
                _state.update { it.copy(showMissedReasonDropdown = action.show) }
            }

            is SurveyAction.SetShowCountyDropdown -> {
                _state.update { it.copy(showCountyDropdown = action.show) }
            }

            is SurveyAction.SetShowSubCountyDropdown -> {
                _state.update { it.copy(showSubCountyDropdown = action.show) }
            }

            is SurveyAction.SetChildGender -> {
                _state.update { it.copy(childGender = action.gender) }
            }

            is SurveyAction.SetChildFirstName -> {
                _state.update { it.copy(childFirstName = action.name) }
            }

            is SurveyAction.SetChildLastName -> {
                _state.update { it.copy(childLastName = action.name ) }
            }

            is SurveyAction.SetHasAttendedSchool -> {
                _state.update { it.copy(hasAttendedSchool = action.attended) }
            }

            is SurveyAction.SetHighestEducationLevel -> {
                _state.update { it.copy(highestEducationLevel = action.level) }
            }

            is SurveyAction.SetLivesWith -> {
                _state.update { it.copy(livesWith = action.livesWith) }
            }

            is SurveyAction.SetParentAge -> {
                _state.update { it.copy(parentAge = action.age) }
            }

            is SurveyAction.SetParentGender -> {
                _state.update { it.copy(parentGender = action.gender) }
            }

            is SurveyAction.SetParentName -> {
                _state.update { it.copy(parentName = action.name) }
            }

            is SurveyAction.SetShowAddChildSheet -> {
                _state.update { it.copy(showAddChildSheet = action.show) }
            }

            is SurveyAction.SetShowChildGenderDropdown -> {
                _state.update { it.copy(showChildGenderDropdown = action.show) }
            }

            is SurveyAction.SetShowGuardianGenderDropdown -> {
                _state.update { it.copy(showGuardianGenderDropdown = action.show) }
            }

            is SurveyAction.SetShowHigherEducationDropdown -> {
                _state.update { it.copy(showHigherEducationDropdown = action.show) }
            }

            is SurveyAction.SetShowLivesWithDropdown -> {
                _state.update { it.copy(showLivesWithDropdown = action.show) }
            }

            is SurveyAction.SetShowParentOrGuardianSheet -> {
                _state.update { it.copy(showParentOrGuardianSheet = action.show) }
            }

            is SurveyAction.SetShowTypeDropdown -> {
                _state.update { it.copy(showTypeDropdown = action.show) }
            }

            is SurveyAction.SetType -> {
                _state.update { it.copy(type = action.type) }
            }

            is SurveyAction.OnAddParent -> {
                _state.update { currentState ->
                    val newParent = Parent(
                        name = currentState.parentName,
                        age = currentState.parentAge,
                        hasAttendedSchool = currentState.hasAttendedSchool,
                        highestEducationLevel = currentState.highestEducationLevel,
                        type = currentState.type,
                    )
                    currentState.copy(
                        parents = currentState.parents.apply { add(newParent) },
                        showParentOrGuardianSheet = false,
                        parentName = "",
                        parentAge = "",
                        hasAttendedSchool = false,
                        highestEducationLevel = ""
                    )
                }
            }

            is SurveyAction.OnRemoveParent -> {
                _state.update { currentState ->
                    currentState.copy(
                        parents = currentState.parents.apply { removeIf { it == action.parent } },
                        showParentOrGuardianSheet = true,
                    )
                }
            }

            is SurveyAction.OnAddChild -> {
                _state.update { currentState ->
                    val newChild = Child(
                        firstName = currentState.childFirstName.trim(),
                        lastName = currentState.childLastName.trim(),
                        gender = currentState.childGender.trim(),
                        age = currentState.childAge.trim(),
                        livesWith = currentState.livesWith.trim(),
                        linkedLearnerId = currentState.linkedLearnerId.trim()
                    )

                    val updatedChildren = currentState.children.toMutableList().apply { add(newChild) }
                    val updatedIsLinkedIdList = currentState.isLinkedIdList.toMutableList().apply {
                        if (newChild.linkedLearnerId.isNotEmpty()) add(newChild.linkedLearnerId)
                    }

                    currentState.copy(
                        children = updatedChildren,
                        isLinkedIdList = updatedIsLinkedIdList,
                        showAddChildSheet = false,
                        childFirstName = "",
                        childLastName = "",
                        childGender = "",
                        childAge = "",
                        livesWith = "",
                        linkedLearnerId = ""
                    )
                }
            }

            is SurveyAction.OnRemoveChild -> {
                _state.update { currentState ->
                    val updatedChildren = currentState.children.filter { it != action.child }.toMutableList()
                    val updatedIsLinkedIdList = currentState.isLinkedIdList.toMutableList().apply {
                        if (action.child.linkedLearnerId.isNotEmpty()) remove(action.child.linkedLearnerId)
                    }

                    currentState.copy(
                        children = updatedChildren,
                        isLinkedIdList = updatedIsLinkedIdList,
                        showAddChildSheet = true
                    )
                }
            }

            is SurveyAction.SetChildAge -> {
                _state.update {
                    it.copy(childAge = action.age)
                }
            }

            is SurveyAction.OnChangeCurrentStep -> {
                updateStep()
            }

            is SurveyAction.OnUpdateCurrentIndex -> {
                _state.update {
                    it.copy(
                        currentStepIndex = action.index,
                        currentStep = it.surveyFlow[action.index]
                    )
                }
                Log.d("SurveyViewModel", "Current step updated to: ${_state.value.currentStep} ${_state.value.currentStepIndex}")
            }

            is SurveyAction.SetHouseholdAssetsAddRemove -> {

                _state.update { currentState ->
                    val updatedAssets = currentState.householdAssets.toMutableList()

                    if (updatedAssets.contains(action.asset)) {
                        updatedAssets.remove(action.asset)
                    } else {
                        updatedAssets.add(action.asset)
                    }

                    currentState.copy(householdAssets = updatedAssets)
                }
            }

            SurveyAction.SubmitSurvey -> {
                submitSurvey()
            }

            is SurveyAction.SetLinkedLearnerId -> {
                _state.update { it.copy(linkedLearnerId = action.learnerId) }
            }
            is SurveyAction.SetShowAvailableLearnerDropdown -> {
                _state.update { it.copy(showAvailableLearnersDropdown = action.show) }
            }

            is SurveyAction.FetchAvailableStudents -> {
                fetchSchoolDetails(
                    organizationId = action.localSchoolInfo.organizationUid,
                    projectId = action.localSchoolInfo.projectUId,
                    schoolId = action.localSchoolInfo.schoolUId,
                )
            }


        }

    }

    private fun updateStep(){
        val flow = when(_state.value.currentStep){
            HouseSurveyStep.CONSENT -> HouseSurveyStep.HOUSEHOLD_BACKGROUND
            HouseSurveyStep.HOUSEHOLD_BACKGROUND -> HouseSurveyStep.FAMILY_MEMBERS
            HouseSurveyStep.FAMILY_MEMBERS -> HouseSurveyStep.PARENTAL_ENGAGEMENT
            HouseSurveyStep.PARENTAL_ENGAGEMENT -> HouseSurveyStep.CHILD_LEARNING_ENVIRONMENT
            HouseSurveyStep.CHILD_LEARNING_ENVIRONMENT -> HouseSurveyStep.CONSENT
        }
        _state.update { it.copy(currentStep = flow) }
    }


    private fun submitSurvey() {
        Log.d("SurveyViewModel", "Survey submitted with state: ${_state.value}")

        viewModelScope.launch {
            val localSchoolInfo = _state.value.localSchoolInfo
            if (localSchoolInfo == null) {
                Log.e("SurveyViewModel", "Cannot submit survey - school info not loaded")

                localDataSource.getSavedCurrentSchoolInfo().collect { schoolInfo ->
                    Log.d("SurveyViewModel", "Fetched school info from local data source")
                    _state.update {
                        it.copy(
                            localSchoolInfo = schoolInfo,
                            errorMessage = "Please try submitting the survey again."
                        )
                    }
                }
                return@launch
            }

            _state.value.children.forEach { child ->
                insertLinkedLearner(child = child)
            }

            val survey = _state.value.toCreateHouseHoldInfo()

            insertPendingSurvey(householdData = survey)

            enqueueUploadHouseholdsWork(
                localSchoolInfo = localSchoolInfo,
                surveyId = survey.id
            )

            resetState()
        }
    }


    private fun fetchSchoolDetails(organizationId: String, projectId: String, schoolId: String, grade: Int? = null) {
        if (organizationId.isEmpty() || projectId.isEmpty() || schoolId.isEmpty()) {
            return
        }

        viewModelScope.launch {
            val data = studentsRepository.getSchoolStudents(
                organizationId = organizationId,
                projectId = projectId,
                schoolId = schoolId,
                studentClass = grade
            ).first()


            when(data.status){
                ResultStatus.INITIAL ,
                ResultStatus.LOADING -> {
                    _state.update {  it.copy(isLoading = true)}
                }
                ResultStatus.SUCCESS -> {

                    _state.update { it ->
                        it.copy(
                            availableLearners = data.data
                                ?.filter { student -> !student.isLinked }
                                ?.filter { student ->
                                    student.id !in _state.value.isLinkedIdList
                                }
                                ?.take(10)
                                ?: emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }

                    Log.d("SurveyViewModel", "Fetched ${_state.value.availableLearners} available learners" )
                }
                ResultStatus.ERROR -> {
                    _state.update {
                        it.copy(
                            error = data.message ?: "Failed to load school details",
                            isLoading = false
                        )
                    }
                }
            }
        }
    }


    private fun insertLinkedLearner(child: Child){
        viewModelScope.launch {
            localDataSource.insertAssignedStudent(
                studentId = child.linkedLearnerId,
                firstName = child.firstName,
                lastName = child.lastName,
                isLinked = true
            )
        }
    }

    private fun getLinkedLearners(){
        TODO()
    }


    private fun insertPendingSurvey(householdData: CreateHouseHoldInfo) {
        viewModelScope.launch {
            localDataSource.insertHouseholdData(createHouseHoldData = householdData)
        }
    }

    private fun enqueueUploadHouseholdsWork(localSchoolInfo: LocalSchoolInfo, surveyId: String) {
        val request  = SubmitHouseholdSurveyWorker.buildRequest(localSchoolInfo = localSchoolInfo, surveyId = surveyId)
        workManager.enqueueUniqueWork(
            uniqueWorkName = "${SubmitHouseholdSurveyWorker.WORK_NAME}_$surveyId",
            existingWorkPolicy =ExistingWorkPolicy.REPLACE,
            request = request
        )
    }


    private fun resetState() {
        _state.update {
            it.copy(
                isSubmitting = false,
                errorMessage = null,
                currentStep = HouseSurveyStep.CONSENT,
                currentStepIndex = 0,
                consentGiven = false,
                county = "",
                subCounty = "",
                ward = "",
                interviewerName = "",
                respondentName = "",
                respondentAge = "",
                isRespondentHeadOfHousehold = false,
                householdHeadName = "",
                householdHeadMobileNumber = "",
                mobileNumberError = null,
                relationshipToHead = "",
                mainLanguageSpokenAtHome = "",
                totalHouseholdMembers = "",
                houseHoldIncomeSource = "",
                hasElectricity = false,
                householdAssets = mutableListOf(),
                discussFrequency = "",
                doAttendMeetings = false,
                doMonitorAttendance = false,
                isSchoolAgeChildrenPresent = false,
                whoHelps = "",
                otherWhoHelps = "",
                hasLearningMaterials = false,
                hasMissedSchool = false,
                isQuietPlaceAvailable = false,
                missedReason = "",
                otherMissedReason = "",
                childGender = "",
                childAge = "",
                childFirstName = "",
                childLastName = "",
                hasAttendedSchool = false,
                highestEducationLevel = "",
                parentAge = "",
                parentGender = "",
                parentName = "",
                type = "",
                parents = mutableListOf(),
                children = mutableListOf()
            )
        }
    }
}
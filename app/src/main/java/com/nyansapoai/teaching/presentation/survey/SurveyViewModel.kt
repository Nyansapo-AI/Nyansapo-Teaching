package com.nyansapoai.teaching.presentation.survey

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.domain.models.survey.Child
import com.nyansapoai.teaching.domain.models.survey.Parent
import com.nyansapoai.teaching.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class SurveyViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(SurveyState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SurveyState()
        )

    fun onAction(action: SurveyAction) {
        when (action) {
            is SurveyAction.SetConsentGiven -> {
                _state.update { it.copy(consentGiven = action.given) }
            }

            is SurveyAction.SetCounty -> {
                _state.update { it.copy(county = action.county) }
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
                _state.update { it.copy(householdHeadName = action.name) }
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
                _state.update { it.copy(relationshipToHead = action.relationship) }
            }

            is SurveyAction.SetRespondentAge -> {
                _state.update { it.copy(respondentAge = action.age) }
            }

            is SurveyAction.SetRespondentName -> {
                _state.update { it.copy(respondentName = action.name) }
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

            is SurveyAction.SetChildName -> {
                _state.update { it.copy(childName = action.name) }
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
                        name = currentState.childName,
                        gender = currentState.childGender,
                        age = currentState.childAge,
                        livesWith = currentState.livesWith
                    )

                    currentState.copy(
                        children = currentState.children.apply { add(newChild) },
                        showAddChildSheet = false,
                        childName = "",
                        childGender = "",
                        childAge = "",
                        livesWith = ""
                    )
                }
            }

            is SurveyAction.OnRemoveChild -> {
                _state.update { currentState ->
                    currentState.copy(
                        children = currentState.children.apply { removeIf { it == action.child } },
                        showAddChildSheet = true
                    )
                }
            }

            is SurveyAction.SetChildAge -> {
                _state.update {
                    it.copy(childAge = action.age)
                }
            }

            SurveyAction.SubmitSurvey -> {
                submitSurvey()
            }
        }

    }

    private fun submitSurvey() {
        Log.d("SurveyViewModel", "Survey submitted with state: ${_state.value}")

    }
}
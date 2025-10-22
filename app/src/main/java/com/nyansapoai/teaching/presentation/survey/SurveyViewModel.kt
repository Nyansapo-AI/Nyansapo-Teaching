package com.nyansapoai.teaching.presentation.survey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        }
    }

}
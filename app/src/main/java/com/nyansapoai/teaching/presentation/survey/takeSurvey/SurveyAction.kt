package com.nyansapoai.teaching.presentation.survey.takeSurvey

import com.nyansapoai.teaching.domain.models.survey.Child
import com.nyansapoai.teaching.domain.models.survey.Parent

sealed interface SurveyAction {
    data class SetInterviewerName(val name: String) : SurveyAction
    data class SetCounty(val county: String) : SurveyAction
    data class SetShowCountyDropdown(val show: Boolean) : SurveyAction
    data class SetShowSubCountyDropdown(val show: Boolean) : SurveyAction
    data class SetSubCounty(val subCounty: String) : SurveyAction
    data class SetWard(val ward: String) : SurveyAction
    data class SetConsentGiven(val given: Boolean) : SurveyAction
    data class SetRespondentName(val name: String) : SurveyAction
    data class SetIsRespondentHeadOfHousehold(val isHead: Boolean) : SurveyAction
    data class SetRespondentAge(val age: String) : SurveyAction
    data class SetHouseholdHeadName(val name: String) : SurveyAction
    data class SetShowRelationshipToHeadDropdown(val show: Boolean) : SurveyAction
    data class SetRelationshipToHead(val relationship: String) : SurveyAction
    data class SetHouseholdHeadMobileNumber(val mobileNumber: String) : SurveyAction
    data class SetMainLanguageSpokenAtHome(val language: String) : SurveyAction
    data class SetShowMainLanguageDropdown(val show: Boolean) : SurveyAction
    data class SetTotalHouseholdMembers(val total: String) : SurveyAction
    data class SetHouseholdIncomeSource(val source: String) : SurveyAction
    data class SetShowIncomeSourceDropdown(val show: Boolean) : SurveyAction
    data class SetHouseholdAssets(val assets: List<String>) : SurveyAction
    data class SetHouseholdAssetsAddRemove(val asset: String) : SurveyAction
    data class SetShowAssetsDropdown(val show: Boolean) : SurveyAction
    data class SetHasElectricity(val hasElectricity: Boolean) : SurveyAction
    data class SetIsSchoolAgeChildrenPresent(val isPresent: Boolean) : SurveyAction
    data class SetWhoHelps(val whoHelps: String) : SurveyAction
    data class SetShowWhoHelpsDropdown(val show: Boolean) : SurveyAction
    data class SetDiscussFrequency(val frequency: String) : SurveyAction
    data class SetShowDiscussWithTeachersDropdown(val show: Boolean) : SurveyAction
    data class SetDoAttendMeetings(val attend: Boolean) : SurveyAction
    data class SetDoMonitorAttendance(val monitor: Boolean) : SurveyAction
    data class SetOtherWhoHelps(val other: String) : SurveyAction
    data class SetHasQuietPlaceAvailable(val isAvailable: Boolean) : SurveyAction
    data class SetHasLearningMaterials(val hasMaterials: Boolean) : SurveyAction
    data class SetHasMissedSchool(val hasMissed: Boolean) : SurveyAction
    data class SetMissedReason(val reason: String) : SurveyAction
    data class SetShowMissedReasonDropdown(val show: Boolean) : SurveyAction
    data class SetOtherMissedReason(val other: String) : SurveyAction
    data class SetShowParentOrGuardianSheet(val show: Boolean) : SurveyAction
    data class SetParentName(val name: String) : SurveyAction
    data class SetParentAge(val age: String) : SurveyAction
    data class SetHasAttendedSchool(val attended: Boolean) : SurveyAction
    data class SetShowHigherEducationDropdown(val show: Boolean) : SurveyAction
    data class SetHighestEducationLevel(val level: String) : SurveyAction
    data class SetParentGender(val gender: String) : SurveyAction
    data class SetShowGuardianGenderDropdown(val show: Boolean) : SurveyAction
    data class SetType(val type: String) : SurveyAction
    data class SetShowTypeDropdown(val show: Boolean) : SurveyAction
    data object OnAddParent : SurveyAction
    data class OnRemoveParent(val parent: Parent) : SurveyAction

    data class SetShowAddChildSheet(val show: Boolean) : SurveyAction
    data class SetChildName(val name: String) : SurveyAction
    data class SetChildGender(val gender: String) : SurveyAction
    data class SetChildAge(val age: String) : SurveyAction
    data class SetShowChildGenderDropdown(val show: Boolean) : SurveyAction
    data class SetLivesWith(val livesWith: String) : SurveyAction
    data class SetShowLivesWithDropdown(val show: Boolean) : SurveyAction
    data object OnAddChild : SurveyAction
    data class OnRemoveChild(val child: Child) : SurveyAction

    data object OnChangeCurrentStep : SurveyAction
    data class OnUpdateCurrentIndex(val index: Int) : SurveyAction
    data object SubmitSurvey: SurveyAction

}
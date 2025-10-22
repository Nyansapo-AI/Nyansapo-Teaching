package com.nyansapoai.teaching.presentation.survey

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

}
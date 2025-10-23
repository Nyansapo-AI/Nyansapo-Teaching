package com.nyansapoai.teaching.presentation.survey

import com.nyansapoai.teaching.domain.models.survey.Child
import com.nyansapoai.teaching.domain.models.survey.CreateHouseHoldInfo
import com.nyansapoai.teaching.domain.models.survey.Parent

data class SurveyState(
    val interviewerName: String = "",
    val countyList: List<County> = County.entries,
    val showCountyDropdown: Boolean = false,
    val showSubCountyDropdown: Boolean = false,
    val county: String = "",
    val subCounty: String = "",
    val ward: String = "",
    val consentGiven: Boolean = false,

    val respondentName: String = "",
    val isRespondentHeadOfHousehold: Boolean = false,
    val respondentAge: String = "",
    val householdHeadName: String = "",
    val showRelationshipToHeadDropdown: Boolean = false,
    val relationshipToHead: String = "",
    val householdHeadMobileNumber: String = "",
    val mobileNumberError: String? = null,
    val mainLanguageSpokenAtHome: String = "",
    val showMainLanguageDropdown: Boolean = false,
    val totalHouseholdMembers: String = "",
    val houseHoldIncomeSource: String = "",
    val showIncomeSourceDropdown: Boolean = false,
    val householdAssets: MutableList<String> = mutableListOf(),
    val showAssetsDropdown: Boolean = false,
    val hasElectricity: Boolean = false,

    val isSchoolAgeChildrenPresent: Boolean = false,
    val showWhoHelpsDropdown: Boolean = false,
    val whoHelps: String = "",
    val otherWhoHelps: String = "",
    val showOtherWhoHelpsDropdown: Boolean = false,
    val showDiscussWithTeachersDropdown: Boolean = false,
    val discussFrequency: String = "",
    val doAttendMeetings: Boolean = false,
    val doMonitorAttendance: Boolean = false,

    val isQuietPlaceAvailable: Boolean = false,
    val hasLearningMaterials: Boolean = false,
    val hasMissedSchool: Boolean = false,
    val missedReason: String = "",
    val showMissedReasonDropdown: Boolean = false,
    val otherMissedReason: String = "",
    val showOtherMissedReasonDropdown: Boolean = false,

    val showParentOrGuardianSheet: Boolean = false,
    val parentName: String = "",
    val parentAge: String = "",
    val hasAttendedSchool: Boolean = false,
    val showHigherEducationDropdown: Boolean = false,
    val highestEducationLevel: String = "",
    val parentGender: String = "",
    val showGuardianGenderDropdown: Boolean = false,
    val type: String = "",
    val showTypeDropdown: Boolean = false,
    val parents: MutableList<Parent> = mutableListOf(),

    val showAddChildSheet: Boolean = false,
    val childName: String = "",
    val childGender: String = "",
    val childAge: String = "",
    val showChildGenderDropdown: Boolean = false,
    val livesWith: String = "",
    val showLivesWithDropdown: Boolean = false,
    val children: MutableList<Child> = mutableListOf(),

    val currentStepIndex: Int = 0,
    val currentStep: HouseSurveyStep = HouseSurveyStep.CONSENT,

    val isSubmitting: Boolean = false,
    val errorMessage: String? = null
){
    fun canSubmit(): Boolean {
        when(currentStep){
            HouseSurveyStep.CONSENT -> {
                return consentGiven &&
                        county.isNotBlank() &&
                        subCounty.isNotBlank() &&
                        ward.isNotBlank() &&
                        interviewerName.isNotBlank()
            }
            HouseSurveyStep.HOUSEHOLD_BACKGROUND -> {
                return if (isRespondentHeadOfHousehold){
                    respondentName.isNotBlank() &&
                            respondentAge.isNotBlank() &&
                            householdHeadName.isNotBlank() &&
                            mobileNumberError == null &&
                            mainLanguageSpokenAtHome.isNotBlank() &&
                            totalHouseholdMembers.isNotBlank() &&
                            houseHoldIncomeSource.isNotBlank()
                } else {
                    respondentName.isNotBlank() &&
                            respondentAge.isNotBlank() &&
                            householdHeadName.isNotBlank() &&
                            relationshipToHead.isNotBlank() &&
                            mobileNumberError == null &&
                            mainLanguageSpokenAtHome.isNotBlank() &&
                            totalHouseholdMembers.isNotBlank() &&
                            houseHoldIncomeSource.isNotBlank()
                }

            }
            HouseSurveyStep.FAMILY_MEMBERS -> {
                return parents.isNotEmpty() || children.isNotEmpty()
            }
            HouseSurveyStep.PARENTAL_ENGAGEMENT -> {
                return if (isSchoolAgeChildrenPresent) {
                    whoHelps.isNotBlank() && discussFrequency.isNotBlank()
                } else {
                    true
                }
            }

            HouseSurveyStep.CHILD_LEARNING_ENVIRONMENT -> {
                return true
            }
        }
    }

    companion object {
        fun SurveyState.toCreateHouseHoldInfo(): CreateHouseHoldInfo{
            return CreateHouseHoldInfo(
                interviewerName = interviewerName,
                village = "testVillage",
                county = county,
                subCounty = subCounty,
                ward = ward,
                consentGiven = consentGiven,
                respondentName = respondentName,
                isHouseholdHead = isRespondentHeadOfHousehold,
                householdHeadName = householdHeadName,
                relationshipToHead = relationshipToHead,
                householdHeadPhone = householdHeadMobileNumber,
                respondentAge = respondentAge.toIntOrNull(),
                mainLanguage = mainLanguageSpokenAtHome,
                children = children.toList(),
                parents = parents.toList(),
                householdMembersCount = totalHouseholdMembers.toIntOrNull(),
                incomeSource = houseHoldIncomeSource,
                hasElectricity = hasElectricity,
                householdAssets = householdAssets.toList()
            )
        }


    }
}


enum class County(val title: String, val subCounties: List<String>) {
    KAKAMEGA(
        title = "Kakamega",
        subCounties = listOf(
            "Lugari",
            "Likuyani",
            "Malava",
            "Lurambi",
            "Navakholo",
            "Mumias East",
            "Mumias West",
            "Matungu",
            "Khwisero",
            "Butere",
            "Ikolomani",
            "Shinyalu"
        )
    ),

    MACHAKOS(
        title = "Machakos",
        subCounties = listOf(
            "Masinga",
            "Yatta",
            "Kangundo",
            "Matungulu",
            "Kathiani",
            "Mavoko",
            "Machakos Town",
            "Mwala"
        )
    )
}


enum class HouseSurveyStep {
    CONSENT,
    HOUSEHOLD_BACKGROUND,
    FAMILY_MEMBERS,
    PARENTAL_ENGAGEMENT,
    CHILD_LEARNING_ENVIRONMENT,
}



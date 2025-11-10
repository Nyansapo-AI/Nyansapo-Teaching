package com.nyansapoai.teaching.presentation.survey.takeSurvey

import com.nyansapoai.teaching.domain.models.school.LocalSchoolInfo
import com.nyansapoai.teaching.domain.models.students.NyansapoStudent
import com.nyansapoai.teaching.domain.models.survey.Child
import com.nyansapoai.teaching.domain.models.survey.ChildLearningEnvironment
import com.nyansapoai.teaching.domain.models.survey.CreateHouseHoldInfo
import com.nyansapoai.teaching.domain.models.survey.Parent
import com.nyansapoai.teaching.domain.models.survey.ParentalEngagement

data class SurveyState(
    val localSchoolInfo: LocalSchoolInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val prematureExitDialog: Boolean = false,


    val interviewerName: String = "",
    val interviewNameError: String? = null,
    val countyList: List<County> = County.entries,
    val showCountyDropdown: Boolean = false,
    val showSubCountyDropdown: Boolean = false,
    val county: String = "",
    val subCounty: String = "",
    val ward: String = "",
    val consentGiven: Boolean? = null,

    val respondentName: String = interviewerName,
    val respondentNameError: String? = null,
    val isRespondentHeadOfHousehold: Boolean? = null,
    val respondentAge: String = "",
    val respondentAgeError: String? = null,
    val householdHeadName: String = "",
    val householdHeadNameError: String? = null,
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
    val hasElectricity: Boolean? = null,
    val showMaritalStatusDropdown: Boolean = false,
    val maritalStatus: String = "",


    val isSchoolAgeChildrenPresent: Boolean? = null,
    val showWhoHelpsDropdown: Boolean = false,
    val whoHelps: String = "",
    val otherWhoHelps: String = "",
    val showOtherWhoHelpsDropdown: Boolean = false,
    val showDiscussWithTeachersDropdown: Boolean = false,
    val discussFrequency: String = "",
    val doAttendMeetings: Boolean? = null,
    val doMonitorAttendance: Boolean? = null,

    val isQuietPlaceAvailable: Boolean? = null,
    val hasLearningMaterials: Boolean? = null,
    val hasMissedSchool: Boolean? = null,
    val missedReason: String = "",
    val showMissedReasonDropdown: Boolean = false,
    val otherMissedReason: String = "",
    val showOtherMissedReasonDropdown: Boolean = false,

    val showParentOrGuardianSheet: Boolean = false,
    val parentName: String = "",
    val parentNameError: String? = null,
    val parentAge: String = "",
    val parentAgeError: String? = null,
    val hasAttendedSchool: Boolean = false,
    val showHigherEducationDropdown: Boolean = false,
    val highestEducationLevel: String = "",
    val parentGender: String = "",
    val showGuardianGenderDropdown: Boolean = false,
    val type: String = "",
    val showTypeDropdown: Boolean = false,
    val parents: MutableList<Parent> = mutableListOf(),

    val showAddChildSheet: Boolean = false,
    val childFirstName: String = "",
    val childLastName: String = "",
    val childGender: String = "",
    val childAge: String = "",
    val childAgeError: String? = null,
    val childGrade: String = "",
    val childGradeError: String? = null,
    val showChildGenderDropdown: Boolean = false,
    val showChildGradeDropdown: Boolean = false,
    val livesWith: String = "",
    val linkedLearnerId: String = "",
    val showAvailableLearnersDropdown: Boolean = false,
    val showLivesWithDropdown: Boolean = false,
    val children: MutableList<Child> = mutableListOf(),
    val isLinkedIdList: MutableList<String> = mutableListOf(),
    val availableLearners: List<NyansapoStudent> = emptyList(),
    val familyMemberError: String? = null,


    val currentStepIndex: Int = 0,
    val currentStep: HouseSurveyStep = HouseSurveyStep.CONSENT,
    val surveyFlow: List<HouseSurveyStep> = listOf(
        HouseSurveyStep.CONSENT,
        HouseSurveyStep.HOUSEHOLD_BACKGROUND,
        HouseSurveyStep.FAMILY_MEMBERS,
        HouseSurveyStep.PARENTAL_ENGAGEMENT,
        HouseSurveyStep.CHILD_LEARNING_ENVIRONMENT
    ),

    val isSubmitting: Boolean = false,
    val errorMessage: String? = null
){
    fun canSubmit(): Boolean {
        when(currentStep){
            HouseSurveyStep.CONSENT -> {
                return consentGiven == true &&
                        interviewerName.isNotBlank() &&
                        interviewNameError == null
            }
            HouseSurveyStep.HOUSEHOLD_BACKGROUND -> {
                return if (isRespondentHeadOfHousehold == true){
                    respondentName.isNotBlank() &&
                            respondentNameError == null &&
                            respondentAge.isNotBlank() &&
                            respondentAgeError == null &&
                            mobileNumberError == null &&
                            mainLanguageSpokenAtHome.isNotBlank() &&
                            totalHouseholdMembers.isNotBlank() &&
                            houseHoldIncomeSource.isNotBlank() &&
                            maritalStatus.isNotBlank()
                } else {
                    respondentName.isNotBlank() &&
                            isRespondentHeadOfHousehold != null &&
                            respondentNameError == null &&
                            respondentAge.isNotBlank() &&
                            respondentAgeError == null &&
                            householdHeadName.isNotBlank() &&
                            householdHeadNameError == null &&
                            relationshipToHead.isNotBlank() &&
                            mobileNumberError == null &&
                            mainLanguageSpokenAtHome.isNotBlank() &&
                            totalHouseholdMembers.isNotBlank() &&
                            houseHoldIncomeSource.isNotBlank()
                }
            }
            HouseSurveyStep.FAMILY_MEMBERS -> {
                return  children.isNotEmpty() && (parents.isNotEmpty() || familyMemberError != null)
            }
            HouseSurveyStep.PARENTAL_ENGAGEMENT -> {
                return true
            }

            HouseSurveyStep.CHILD_LEARNING_ENVIRONMENT -> {
                return isQuietPlaceAvailable != null && hasLearningMaterials != null
            }
        }
    }

    companion object {
        fun SurveyState.toCreateHouseHoldInfo(): CreateHouseHoldInfo{
            return CreateHouseHoldInfo(
                interviewerName = interviewerName,
                villageId = localSchoolInfo?.schoolUId,
                ward = ward,
                consentGiven = consentGiven ?: false,
                respondentName = respondentName,
                isHouseholdHead = isRespondentHeadOfHousehold ?: false,
                householdHeadName = householdHeadName,
                relationshipToHead = relationshipToHead,
                householdHeadPhone = householdHeadMobileNumber,
                respondentAge = respondentAge.toIntOrNull(),
                mainLanguage = mainLanguageSpokenAtHome,
                maritalStatus = maritalStatus,
                children = children.toList(),
                parents = parents.toList(),
                householdMembersCount = totalHouseholdMembers.toIntOrNull(),
                incomeSource = houseHoldIncomeSource,
                hasElectricity = hasElectricity,
                householdAssets = householdAssets.toList(),
                parentalEngagement = if (isSchoolAgeChildrenPresent == true){
                    ParentalEngagement(
                        hasSchoolAgeChild = true,
                        homeworkHelper = if (whoHelps == "Other") otherWhoHelps else whoHelps,
                        teacherDiscussionFrequency = discussFrequency,
                        attendsSchoolMeetings = doAttendMeetings,
                        monitorsAttendance = doMonitorAttendance
                    )
                } else null,
                childLearningEnvironment = ChildLearningEnvironment(
                    hasQuietPlaceToStudy = isQuietPlaceAvailable ?: false,
                    hasBooksOrMaterials = hasLearningMaterials ?: false
                )
            )
        }


        val demoSurveyState = SurveyState(
            interviewerName = "John Doe",
            county = County.KAKAMEGA.title,
            subCounty = "Lurambi",
            ward = "Central Ward",
            consentGiven = true,

            respondentName = "Jane Smith",
            isRespondentHeadOfHousehold = true,
            respondentAge = "35",
            householdHeadMobileNumber = "+254712345678",
            householdHeadName = "Jane Smith",
            mainLanguageSpokenAtHome = "Swahili",
            totalHouseholdMembers = "5",
            houseHoldIncomeSource = "Farming",
            householdAssets = mutableListOf("Radio", "Bicycle"),
            hasElectricity = true,

            children = mutableListOf(
                Child(
                    firstName = "Alice",
                    lastName = "Smith",
                    gender = "Female",
                    age = "10",
                    linkedLearnerId = "learn123",
                    livesWith = "Both Parents"
                )
            ),

            parents = mutableListOf(
                Parent(
                    name = "Jane Smith",
                    age = "35",
//                    gender = "Female",
                    hasAttendedSchool = true,
                    highestEducationLevel = "Secondary",
                    type = "Mother"
                )
            ),

            isSchoolAgeChildrenPresent = true,
            whoHelps = "Mother",
            discussFrequency = "Monthly",
            doAttendMeetings = true,
            doMonitorAttendance = true,

            isQuietPlaceAvailable = true,
            hasLearningMaterials = true,
            currentStep = HouseSurveyStep.CONSENT
        )
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



package com.nyansapoai.teaching.domain.models.survey

data class HouseHoldInfo(
    val id: String = "",
    val interviewerName: String = "",
    val interviewDate: String = "",
    val county: String = "",
    val subCounty: String = "",
    val ward: String = "",
    val consentGiven: Boolean = true,
    val respondentName: String? = null,
    val isHouseholdHead: Boolean = false,
    val householdHeadName: String? = null,
    val relationshipToHead: String? = null,
    val householdHeadPhone: String? = null,
    val respondentAge: Int? = null,
    val mainLanguage: String? = null,
    val children: List<Child> = emptyList(),
    val parents: List<Parent> = emptyList(),
    val maritalStatus: String? = null,
    val householdMembersCount: Int? = null,
    val incomeSource: String? = null,
    val hasElectricity: String? = null,
    val householdAssets: List<String> = emptyList(),
    val parentalEngagement: ParentalEngagement? = null,
    val childLearningEnvironment: ChildLearningEnvironment? = null
)

data class ParentalEngagement(
    val hasSchoolAgeChild: Boolean,
    val homeworkHelper: String?,
    val teacherDiscussionFrequency: String?,
    val attendsSchoolMeetings: Boolean?,
    val monitorsAttendance: Boolean?
)

data class ChildLearningEnvironment(
    val hasQuietPlaceToStudy: Boolean,
    val hasBooksOrMaterials: Boolean,
    val missedSchoolLastMonth: Boolean,
    val reasonForMissingSchool: String?
)

data class Parent(
    val name: String,
    val age: String,
    val type: String,
    val hasAttendedSchool: Boolean,
    val highestEducationLevel: String
)

data class Child(
    val name: String,
    val gender: String,
    val age: String,
    val livesWith: String
)
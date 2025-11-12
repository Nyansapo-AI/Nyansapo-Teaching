package com.nyansapoai.teaching.domain.models.survey

import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@IgnoreExtraProperties
data class HouseHoldInfo(
    val id: String = "",
    val interviewerName: String = "",
    val interviewDate: String = "",
    val villageId: String? = null,
    val village: String = "testVillage",
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
    val hasElectricity: Boolean? = null,
    val householdAssets: List<String> = emptyList(),
    val parentalEngagement: ParentalEngagement? = null,
    val childLearningEnvironment: ChildLearningEnvironment? = null
)

data class CreateHouseHoldInfo @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String = Uuid.random().toString(),
    val interviewerName: String = "",
    val interviewDate: String = Clock.System.now().toString(),
    val villageId: String? = null,
    val village: String = "testVillage",
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
    val hasElectricity: Boolean? = null,
    val householdAssets: List<String> = emptyList(),
    val parentalEngagement: ParentalEngagement? = null,
    val childLearningEnvironment: ChildLearningEnvironment? = null
)

data class ParentalEngagement(
    val hasSchoolAgeChild: Boolean = false,
    val homeworkHelper: String? = null,
    val teacherDiscussionFrequency: String? = null,
    val attendsSchoolMeetings: Boolean? = null,
    val monitorsAttendance: Boolean? = null
)

data class ChildLearningEnvironment(
    val hasQuietPlaceToStudy: Boolean = false,
    val hasBooksOrMaterials: Boolean = false,
)

data class Parent(
    val name: String = "",
    val age: String = "",
    val type: String = "",
    val hasAttendedSchool: Boolean = false,
    val highestEducationLevel: String = "",
)

data class Child(
    val firstName: String = "",
    val lastName: String = "",
    val gender: String = "",
    val grade: String = "",
    val age: String = "",
    val livesWith: String = "",
    val linkedLearnerId: String = "",
    val wasAssessedIn2024: Boolean = false,
    val wasAboveStoryLevelIn2024: Boolean = false
)
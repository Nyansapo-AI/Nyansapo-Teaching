package com.nyansapoai.teaching.navigation

import kotlinx.serialization.Serializable


@Serializable
data object GetStartedPage

@Serializable
data object SignInPage

@Serializable
data object AuthControllerPage

@Serializable
data class OTPPage(val phoneNumber: String)

@Serializable
data object OnboardingPage

@Serializable
data object HomePage

@Serializable
data object CreateAssessmentsPage

@Serializable
data class IndividualAssessmentPage(val assessmentId: String)

@Serializable
data class ConductAssessmentPage(
    val assessmentId: String,
    val assessmentType: String,
    val studentId: String,
    val studentName: String,
    val assessmentNo: Int
)

@Serializable
data class LiteracyResultsPage(
    val assessmentId: String,
    val studentId: String,
)

@Serializable
data class AssessmentResultsPage(
    val assessmentId: String,
    val studentId: String,
    val studentName: String,
    val level: String,
    val grade: Int,
    val assessmentName: String,
    val assessmentType: String,
)

@Serializable
data class CollectAttendancePage(
    val date: String,
    val schoolId: String,
    val organizationId: String,
    val projectId: String,
)

@Serializable
data object AddHouseHoldPage

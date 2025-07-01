package com.nyansapoai.teaching.presentation.navigation

import kotlinx.serialization.Serializable


@Serializable
data object GetStartedPage

@Serializable
data object SignInPage

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
    val assessmentNo: Int
)

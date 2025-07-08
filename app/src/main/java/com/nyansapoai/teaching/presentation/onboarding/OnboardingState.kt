package com.nyansapoai.teaching.presentation.onboarding

data class OnboardingState(
    val currentStep: Int = 1,
    val selectedOrganization: OnboardingOrganizationState? = null,
    val selectedProject: OnboardingProjectState? = null,
    val selectedSchool: OnboardingSchoolState? = null,
    val selectedCamp: OnboardingCampState? = null,
    )

data class OnboardingOrganizationState(
    val name: String,
    val id: String,
)

data class OnboardingProjectState(
    val name: String,
    val id: String,
)

data class OnboardingSchoolState(
    val name: String,
    val id: String,
)

data class OnboardingCampState(
    val name: String,
    val id: String,
)
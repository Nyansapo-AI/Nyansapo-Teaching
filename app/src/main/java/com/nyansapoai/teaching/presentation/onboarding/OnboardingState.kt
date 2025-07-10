package com.nyansapoai.teaching.presentation.onboarding

import com.nyansapoai.teaching.domain.models.organization.NyansapoOrganization
import com.nyansapoai.teaching.domain.models.project.NyansapoProject
import com.nyansapoai.teaching.domain.models.school.NyansapoSchool
import com.nyansapoai.teaching.domain.models.user.NyansapoUser

data class OnboardingState(
    val currentStep: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val userData: NyansapoUser? = null,
    val selectedOrganization: NyansapoOrganization? = null,
    val selectedProject: NyansapoProject? = null,
    val selectedSchool: NyansapoSchool? = null,
    val selectedCamp: OnboardingCampState? = null,
    val canContinue: Boolean = false
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
package com.nyansapoai.teaching.presentation.onboarding

data class OnboardingState(
    val currentStep: Int = 1,
    val selectedOrganization: OrganizationUI? = null,

    val paramTwo: List<String> = emptyList(),
)



data class OrganizationUI(
    val name: String,
    val id: String,
)
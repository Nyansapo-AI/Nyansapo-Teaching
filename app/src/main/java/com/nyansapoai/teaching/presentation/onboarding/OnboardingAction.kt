package com.nyansapoai.teaching.presentation.onboarding

sealed interface OnboardingAction {
    data class OnStepChange(val step: Int): OnboardingAction

    data class OnSelectOrganization(val organizationUI: OnboardingOrganizationState): OnboardingAction

    data class OnSelectSchool(val school: OnboardingSchoolState): OnboardingAction

    data class OnSelectProject(val project: OnboardingProjectState): OnboardingAction

    data class OnSelectCamp(val camp: OnboardingCampState): OnboardingAction
}
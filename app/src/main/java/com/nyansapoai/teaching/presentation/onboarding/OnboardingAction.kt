package com.nyansapoai.teaching.presentation.onboarding

import com.nyansapoai.teaching.domain.models.organization.NyansapoOrganization
import com.nyansapoai.teaching.domain.models.project.NyansapoProject
import com.nyansapoai.teaching.domain.models.school.NyansapoSchool

sealed interface OnboardingAction {
    data class OnStepChange(val step: Int): OnboardingAction

    data class OnSelectOrganization(val organizationUI: NyansapoOrganization): OnboardingAction

    data class OnSelectSchool(val school: NyansapoSchool): OnboardingAction

    data class OnSelectProject(val project: NyansapoProject): OnboardingAction

    data class OnSelectCamp(val camp: OnboardingCampState): OnboardingAction

    data class  OnContinue(val onSuccess: () -> Unit): OnboardingAction

}
package com.nyansapoai.teaching.presentation.onboarding

sealed interface OnboardingAction {
    data class OnStepChange(val step: Int): OnboardingAction
}
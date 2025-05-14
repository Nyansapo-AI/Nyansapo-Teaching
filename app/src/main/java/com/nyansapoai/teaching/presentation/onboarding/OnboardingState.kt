package com.nyansapoai.teaching.presentation.onboarding

data class OnboardingState(
    val currentStep: Int = 0,
    val paramTwo: List<String> = emptyList(),
)
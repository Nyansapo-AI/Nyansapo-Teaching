package com.nyansapoai.teaching.presentation.onboarding

data class OnboardingState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)
package com.nyansapoai.teaching.presentation.getStarted

data class GetStartedState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)
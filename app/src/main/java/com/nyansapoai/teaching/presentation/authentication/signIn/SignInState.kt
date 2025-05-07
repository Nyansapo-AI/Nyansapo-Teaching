package com.nyansapoai.teaching.presentation.authentication.signIn

data class SignInState(
    val name: String = "",
    val phoneNumber: String = "",
    val submitEnabled: Boolean = true
)
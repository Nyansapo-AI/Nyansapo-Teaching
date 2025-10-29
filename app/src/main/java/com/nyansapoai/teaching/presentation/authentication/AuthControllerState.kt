package com.nyansapoai.teaching.presentation.authentication

data class AuthControllerState(
    val isUserLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
)
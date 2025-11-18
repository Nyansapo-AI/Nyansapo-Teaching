package com.nyansapoai.teaching.presentation.authentication.otp

data class OTPState(
    val otpCde: String = "",
    val canSubmit: String = "",
    val isLoading: Boolean = false
)
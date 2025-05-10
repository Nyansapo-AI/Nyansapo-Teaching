package com.nyansapoai.teaching.presentation.authentication.otp

sealed interface OTPAction {
    data class  OnOTPCOdeChange(val code: String): OTPAction
}
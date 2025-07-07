package com.nyansapoai.teaching.presentation.authentication.otp

import androidx.compose.runtime.Composable

sealed interface OTPAction {
    data class  OnOTPCOdeChange(val code: String): OTPAction

    data class OnCanSubmitChange(val canSubmit: Boolean): OTPAction

    data class OnMessageChange(val message: String): OTPAction

    data class OnSubmit(val onSuccess: @Composable () -> Unit = {}): OTPAction
}
package com.nyansapoai.teaching.presentation.authentication.signIn

import androidx.compose.runtime.Composable

sealed interface SignInAction {
    data class OnNameChange(val name: String): SignInAction

    data class OnPhoneNumberChange(val phoneNumber: String): SignInAction

    data class OnSubmit(val onSuccess: @Composable () -> Unit = {}): SignInAction
}
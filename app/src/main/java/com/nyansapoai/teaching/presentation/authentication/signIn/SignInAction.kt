package com.nyansapoai.teaching.presentation.authentication.signIn

sealed interface SignInAction {
    data class OnNameChange(val name: String): SignInAction

    data class OnPhoneNumberChange(val phoneNumber: String): SignInAction

    data class OnSubmit(val onSuccess: () -> Unit = {}): SignInAction
}
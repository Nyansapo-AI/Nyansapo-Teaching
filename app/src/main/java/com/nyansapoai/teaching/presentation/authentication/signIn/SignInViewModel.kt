package com.nyansapoai.teaching.presentation.authentication.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class SignInViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(SignInState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SignInState()
        )

    fun onAction(action: SignInAction) {
        when (action) {
            is SignInAction.OnNameChange -> {
                _state.update { it.copy(name = action.name) }
            }
            is SignInAction.OnPhoneNumberChange -> {
                _state.update { it.copy(phoneNumber = action.phoneNumber) }
            }

            is SignInAction.OnSubmit -> {
                onSubmitSignInForm()
            }
        }
    }

    private fun onSubmitSignInForm() {

    }

}
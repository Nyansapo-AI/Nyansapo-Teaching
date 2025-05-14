package com.nyansapoai.teaching.presentation.authentication.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SignInViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()


    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber = _phoneNumber.asStateFlow()


    private val isPhoneNumberValid = phoneNumber
        .map { Utils.isValidPhoneNumber(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    private val isNameEmpty = name
        .map { it.isEmpty() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            true
        )


    val canSubmit = combine(
        isPhoneNumberValid,
        isNameEmpty
    ){ isNumberValid, isNameEmpty ->
        isNumberValid && !isNameEmpty
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )


    fun onAction(action: SignInAction) {
        when (action) {
            is SignInAction.OnNameChange -> {
                _name.value = action.name
            }
            is SignInAction.OnPhoneNumberChange -> {
                _phoneNumber.value = action.phoneNumber
            }

            is SignInAction.OnSubmit -> {
//                onSubmitSignInForm()
                action.onSuccess
            }
        }
    }

    private fun onSubmitSignInForm() {

    }

}
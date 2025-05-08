package com.nyansapoai.teaching.presentation.authentication.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class OTPViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(OTPState())
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
            initialValue = OTPState()
        )

    fun onAction(action: OTPAction) {
        when (action) {
            is OTPAction.OnOTPCOdeChange -> {
                _state.update { it.copy(otpCde = action.code) }
            }
        }
    }

}
package com.nyansapoai.teaching.presentation.authentication.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OTPViewModel : ViewModel() {

    init {
        startTimer()
    }

    private var hasLoadedInitialData = false

    private val _otpCode = MutableStateFlow("")
    val otpCode = _otpCode.asStateFlow()

    private val _timer = MutableStateFlow(60)
    val timer = _timer.asStateFlow()

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()



    val canResendOTPRequest = timer
        .map { it == 0 }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            false
        )


    private val _canSubmit = MutableStateFlow(false)
    val canSubmit = _canSubmit.asStateFlow()



    val isOTPComplete = _otpCode
        .map { it.length  == 6  }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    fun onAction(action: OTPAction) {
        when (action) {
            is OTPAction.OnOTPCOdeChange -> {
                _otpCode.value = action.code
            }

            is OTPAction.OnCanSubmitChange -> {
                _canSubmit.value = action.canSubmit
            }

            is OTPAction.OnMessageChange -> {
                _message.value = action.message
            }

            is OTPAction.OnSubmit -> {
                action.onSuccess
            }
        }
    }


    private fun startTimer() {
        viewModelScope.launch(Dispatchers.Main) {
            while (_timer.value > 0) {
                delay(1000L)
                _timer.value.let {
                    _timer.update { it - 1 }
                }
            }
        }
    }



}
package com.nyansapoai.teaching.presentation.authentication.otp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nyansapoai.teaching.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
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



    private val _timer = MutableStateFlow(30)
    val timer = _timer.asStateFlow()


    val canResendOTPRequest = timer
        .map { it == 0 }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            false
        )



    val canSubmit = _otpCode
        .map { it.length  == 4  }
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
        }
    }


    private fun startTimer() {
        viewModelScope.launch(Dispatchers.Main) {
            while (_timer.value > 0) {
                delay(1000L)
                _timer.value.let {
                    _timer.update { it - 1 }
//                    Log.d("timer", "Timer: $it")
                }
            }
        }
    }



}
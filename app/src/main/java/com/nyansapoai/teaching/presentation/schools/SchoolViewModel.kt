package com.nyansapoai.teaching.presentation.schools

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyansapoai.teaching.data.remote.user.UserRepository
import com.nyansapoai.teaching.utils.ResultStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class SchoolViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(SchoolState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                _state.update {
                    it.copy(greeting = getTimeBasedGreeting())
                }

                fetchCurrentUserDetails()

                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SchoolState()
        )

    fun onAction(action: SchoolAction) {
        when (action) {
            is SchoolAction.OnShowSchoolSelector -> {
                _state.update {
                    it.copy(
                        showSchoolSelector = action.show
                    )
                }
            }
        }
    }


    fun getTimeBasedGreeting(): String {
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val hourOfDay = currentDateTime.hour

        return when {
            hourOfDay < 12 -> "Good Morning"
            hourOfDay < 17 -> "Good Afternoon"
            hourOfDay < 21 -> "Good Evening"
            else -> "Good Night"
        }
    }

    private fun fetchCurrentUserDetails(){
        viewModelScope.launch(Dispatchers.IO) {
            val userData = userRepository.getUserDetails()

            Log.d("user data", "user information: $userData")

            when(userData.status){
                ResultStatus.INITIAL ,
                ResultStatus.LOADING -> {
                    _state.update { it.copy(isLoading = true) }
                }
                ResultStatus.SUCCESS -> {
                    _state.update {
                        it.copy(
                            user = userData.data
                        )
                    }
                }
                ResultStatus.ERROR -> {

                }
            }

        }
    }

}
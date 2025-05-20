package com.nyansapoai.teaching.presentation.camps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class CampViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(CampState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                _state.update {
                    it.copy(greeting = getTimeBasedGreeting())
                }

                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CampState()
        )

    fun onAction(action: CampAction) {
        when (action) {
            else -> TODO("Handle actions")
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

}
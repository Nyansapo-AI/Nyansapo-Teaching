package com.nyansapoai.teaching.presentation.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.nyansapoai.teaching.data.local.LocalDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AuthControllerViewModel(
    private val localDataSource: LocalDataSource,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {



    private val _state = MutableStateFlow(AuthControllerState())
    val state = _state
        .onStart {
            _state.update { it.copy(isUserLoggedIn = firebaseAuth.currentUser != null) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AuthControllerState()
        )

    fun onAction(action: AuthControllerAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}
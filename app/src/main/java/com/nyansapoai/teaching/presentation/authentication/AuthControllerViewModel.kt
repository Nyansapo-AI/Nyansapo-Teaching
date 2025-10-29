package com.nyansapoai.teaching.presentation.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.nyansapoai.teaching.data.local.LocalDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AuthControllerViewModel(
    private val localDataSource: LocalDataSource,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _state = MutableStateFlow(
        AuthControllerState(isLoading = true, isUserLoggedIn = firebaseAuth.currentUser != null)
    )

    val state = _state.asStateFlow()

    private val authListener = FirebaseAuth.AuthStateListener { auth ->
        _state.update {
            it.copy(
                isUserLoggedIn = auth.currentUser != null,
                isLoading = false
            )
        }
    }

    init {
        firebaseAuth.addAuthStateListener(authListener)
    }

    override fun onCleared() {
        firebaseAuth.removeAuthStateListener(authListener)
        super.onCleared()
    }

    fun onAction(action: AuthControllerAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}
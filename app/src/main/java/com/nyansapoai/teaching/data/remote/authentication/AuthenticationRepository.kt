package com.nyansapoai.teaching.data.remote.authentication

import com.nyansapoai.teaching.domain.dto.SignInWithPhoneNumberDTO

interface AuthenticationRepository {
    suspend fun signInWithPhoneNumber(request: SignInWithPhoneNumberDTO)

    suspend fun checkIfPhoneNumberIsRegistered(phone: String): Boolean

    suspend fun signOut()
}
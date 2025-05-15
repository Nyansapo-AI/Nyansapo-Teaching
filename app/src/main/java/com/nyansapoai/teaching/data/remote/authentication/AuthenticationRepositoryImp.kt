package com.nyansapoai.teaching.data.remote.authentication

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.nyansapoai.teaching.domain.dto.SignInWithPhoneNumberDTO
import java.util.concurrent.TimeUnit

class AuthenticationRepositoryImp(
    private val auth: FirebaseAuth,
): AuthenticationRepository {
    override suspend fun signInWithPhoneNumber(request: SignInWithPhoneNumberDTO) {
    }
}
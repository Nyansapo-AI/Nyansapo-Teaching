package com.nyansapoai.teaching.presentation.authentication.otp.components

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

@Composable
fun OTPImplementation(
    phoneNumber: String,
    code: String? = null,
    canVerify: Boolean = false,
    onVerificationCompleted: () -> Unit = {},
    onVerificationFailed: (String) -> Unit = {},
    onCodeSent: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context as? Activity ?: return
    val auth = FirebaseAuth.getInstance()

    val verificationId = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        auth.firebaseAuthSettings.setAppVerificationDisabledForTesting(false)
        auth.setLanguageCode("en")
    }

    LaunchedEffect(code, canVerify, verificationId.value) {
        if (!code.isNullOrEmpty() && canVerify && verificationId.value.isNotEmpty()) {
            verifyPhoneNumberWithCode(
                verificationId = verificationId.value,
                code = code,
                auth = auth,
                onSuccess = onVerificationCompleted,
                onFailure = { onVerificationFailed(it) }
            )
        }
    }

    // Start phone verification when component is displayed
    LaunchedEffect(phoneNumber) {
        startPhoneNumberVerification(
            phoneNumber = phoneNumber,
            activity = activity,
            auth = auth,
            onVerificationCompleted = onVerificationCompleted,
            onVerificationFailed = { onVerificationFailed(it) },
            onCodeSent = { sentVerificationId ->
                verificationId.value = sentVerificationId
                onCodeSent(sentVerificationId)
            }
        )
    }
}

private fun startPhoneNumberVerification(
    phoneNumber: String,
    activity: Activity,
    auth: FirebaseAuth,
    onVerificationCompleted: () -> Unit,
    onVerificationFailed: (String) -> Unit,
    onCodeSent: (String) -> Unit
) {
    val options = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber(phoneNumber)
        .setTimeout(60L, TimeUnit.SECONDS)
        .setActivity(activity)
        .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(
                    auth = auth,
                    credential = credential,
                    onSuccess = onVerificationCompleted,
                    onFailure = onVerificationFailed
                )
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.e("PhoneAuth", "Verification Failed: ${e.message}")
                onVerificationFailed(e.message ?: "Unknown error")
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("PhoneAuth", "Code Sent: $verificationId")
                onCodeSent(verificationId)
            }
        })
        .build()


    PhoneAuthProvider.verifyPhoneNumber(options)
}

private fun verifyPhoneNumberWithCode(
    verificationId: String,
    code: String,
    auth: FirebaseAuth,
    onSuccess: () -> Unit,
    onFailure: (String) -> Unit
) {
    val credential = PhoneAuthProvider.getCredential(verificationId, code)
    signInWithPhoneAuthCredential(auth, credential, onSuccess, onFailure)
}

private fun signInWithPhoneAuthCredential(
    auth: FirebaseAuth,
    credential: PhoneAuthCredential,
    onSuccess: () -> Unit,
    onFailure: (String) -> Unit
) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result?.user
                Log.d("PhoneAuth", "Authentication successful: ${user?.phoneNumber}")
                onSuccess()
            } else {
                val errorMessage = task.exception?.message ?: "Authentication failed"
                Log.e("PhoneAuth", "Authentication failed: $errorMessage")
                onFailure(errorMessage)
            }
        }
}



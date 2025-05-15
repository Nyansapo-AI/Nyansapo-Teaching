package com.nyansapoai.teaching.presentation.authentication.otp.components

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

@Composable
fun OTPImplementation(
    phoneNumber: String
) {
    val context = LocalContext.current
    val activity = context as? Activity ?: return

    val auth = FirebaseAuth.getInstance()

    auth.firebaseAuthSettings.setAppVerificationDisabledForTesting(false)
    auth.setLanguageCode("en")

    fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(auth, credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.e("PhoneAuth", "Verification Failed: ${e.message}")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    Log.d("PhoneAuth", "Code Sent: $verificationId")

                    verifyPhoneNumberWithCode(
                        verificationId = verificationId,
                        code = "343433",
                        auth = auth
                    )

                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }




    LaunchedEffect(true) {
        startPhoneNumberVerification(phoneNumber= phoneNumber)
    }


}


fun signInWithPhoneAuthCredential(auth: FirebaseAuth, credential: PhoneAuthCredential) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result?.user
                Log.d("PhoneAuth", "Authentication successful: ${user?.phoneNumber}")
            } else {
                Log.e("PhoneAuth", "Authentication failed: ${task.exception?.message}")
            }
        }

}

fun verifyPhoneNumberWithCode(verificationId: String, code: String, auth: FirebaseAuth) {
    val credential = PhoneAuthProvider.getCredential(verificationId, code)
    signInWithPhoneAuthCredential(auth, credential)
}


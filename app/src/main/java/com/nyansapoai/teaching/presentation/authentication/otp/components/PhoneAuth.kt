package com.nyansapoai.teaching.presentation.authentication.otp.components

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

object PhoneAuth {
    private val auth = FirebaseAuth.getInstance()

    init {
        auth.firebaseAuthSettings.setAppVerificationDisabledForTesting(false)
    }

    @Composable
    fun StartPhoneNumberVerification(
        phoneNumber: String,
        code: String,
        canVerify: Boolean = false
    ){
        val context = LocalContext.current
        val activity = context as? Activity ?: return


        LaunchedEffect(true) {
            start(
                activity = activity,
                phoneNumber = phoneNumber,
                context = context,
                code = code,
                canVerify = canVerify
            )
        }

        LaunchedEffect(canVerify) {
            start(
                activity = activity,
                phoneNumber = phoneNumber,
                context = context,
                code = code,
                canVerify = canVerify
            )
        }

    }


    fun start(activity: Activity, phoneNumber: String, context: Context, code: String, canVerify: Boolean){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(auth = auth, credential = credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.e("PhoneAuth", "Verification Failed: ${e.message}")

                    val toast = Toast.makeText(context,"Verification Failed", Toast.LENGTH_LONG)
                    toast.show()

                }


                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    Log.d("PhoneAuth", "Code Sent: $verificationId")

                    verifyPhoneNumberWithCode(
                        verificationId = verificationId,
                        code = code,
                        auth = auth
                    )

                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
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
        signInWithPhoneAuthCredential(
            auth,
            credential
        )
    }
}
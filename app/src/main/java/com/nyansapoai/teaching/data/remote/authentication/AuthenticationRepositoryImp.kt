package com.nyansapoai.teaching.data.remote.authentication

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nyansapoai.teaching.domain.dto.SignInWithPhoneNumberDTO
import kotlinx.coroutines.tasks.await

class AuthenticationRepositoryImp(
    private val auth: FirebaseAuth,
    private val firebaseDb: FirebaseFirestore,
): AuthenticationRepository {

    private val userCollection = "user"

    override suspend fun signInWithPhoneNumber(request: SignInWithPhoneNumberDTO) {
    }

    override suspend fun checkIfPhoneNumberIsRegistered(phone: String): Boolean {
        val documentSnapshot = firebaseDb.collection(userCollection)
            .whereEqualTo("phone", phone)
            .get()
            .await()

        return documentSnapshot.documents.isNotEmpty()

    }

    override suspend fun signOut() {
        auth.signOut()
        Log.d("Authentication User", "User signed out: ${auth.currentUser}")
    }
}
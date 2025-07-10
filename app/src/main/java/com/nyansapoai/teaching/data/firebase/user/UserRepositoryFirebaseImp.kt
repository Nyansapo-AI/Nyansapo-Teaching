package com.nyansapoai.teaching.data.firebase.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.nyansapoai.teaching.data.remote.user.UserRepository
import com.nyansapoai.teaching.domain.models.user.NyansapoUser
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryFirebaseImp(
    private val auth: FirebaseAuth,
    private val firebaseDb: FirebaseFirestore
): UserRepository {

    private val userCollection = "user"

    override suspend fun getUserDetails(): Results<NyansapoUser> = withContext(Dispatchers.IO) {
        val uid = auth.currentUser?.uid

        if (uid == null){
            return@withContext Results.error(msg = "User not authenticated")
        }

        return@withContext try {
            val documentSnapshot = firebaseDb.collection(userCollection)
                .document(uid)
                .get()
                .await()

            val userData = documentSnapshot.toObject<NyansapoUser>()
            Results.success(data = userData)
        }catch (e: Exception){
            Results.error(msg = e.message ?: "Can not get User information")
        }
    }
}
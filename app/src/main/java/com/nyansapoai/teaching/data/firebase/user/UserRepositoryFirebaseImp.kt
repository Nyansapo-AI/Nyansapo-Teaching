package com.nyansapoai.teaching.data.firebase.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.nyansapoai.teaching.data.remote.user.UserRepository
import com.nyansapoai.teaching.domain.models.user.NyansapoUser
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class UserRepositoryFirebaseImp(
    private val auth: FirebaseAuth,
    private val firebaseDb: FirebaseFirestore
): UserRepository {

    private val userCollection = "user"

    override fun getUserDetails(): Flow<Results<NyansapoUser>> = flow {
        val uid = auth.currentUser?.uid

        if (uid == null){
            emit(Results.error(msg = "User not authenticated"))
            return@flow
        }

        val documentSnapshot = firebaseDb.collection(userCollection)
            .document(uid)
            .get()
            .await()

        val userData = documentSnapshot.toObject<NyansapoUser>()
        emit(Results.success(data = userData))
    }.catch { e ->
        emit(Results.error(msg = e.message ?: "Can not get User information"))
    }
}
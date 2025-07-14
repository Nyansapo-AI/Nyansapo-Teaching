package com.nyansapoai.teaching.data.firebase.media

import com.google.firebase.storage.FirebaseStorage
import com.nyansapoai.teaching.data.remote.media.MediaRepository
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseMediaRepositoryImpl(
    private val firebaseStorage: FirebaseStorage
): MediaRepository {

    val mediaStorageRef = firebaseStorage.reference.child("media")

    val audioStorageRef = firebaseStorage.reference.child("audio")


    override suspend fun saveImage(imageByteArray: ByteArray, folder: String): Results<String> {

        val deferred = CompletableDeferred<Results<String>>()

        firebaseStorage.reference.child(folder)
            .child("image_${imageByteArray.take(7)}")
            .putBytes(imageByteArray)
            .addOnFailureListener {
                deferred.complete(Results.error(msg = it.message ?: "Error uploading image"))
            }
            .addOnSuccessListener { taskSnapshot ->

                taskSnapshot.storage.downloadUrl
                    .addOnSuccessListener { uri ->
                        uri?.let {
                            deferred.complete(Results.success(data = uri.toString()))
                        } ?: run {
                            deferred.complete(Results.error(msg = "Failed to get download URL"))
                        }
                    }
                    .addOnFailureListener {
                        deferred.complete(Results.error(msg = it.message ?: "Error getting download URL"))
                    }
            }

        return withContext(Dispatchers.IO) {
            deferred.await()
        }
    }

    override suspend fun saveAudio(audioByteArray: ByteArray, folder: String): Results<String> {

        val deferred = CompletableDeferred<Results<String>>()

        firebaseStorage.reference.child(folder)
            .child("audio_${audioByteArray.take(7).hashCode()}")
            .putBytes(audioByteArray)
            .addOnFailureListener {
                deferred.complete(Results.error(msg = it.message ?: "Error uploading audio"))
            }
            .addOnSuccessListener { taskSnapshot ->

                taskSnapshot.storage.downloadUrl
                    .addOnSuccessListener { uri ->
                        uri?.let {
                            deferred.complete(Results.success(data = uri.toString()))
                        } ?: run {
                            deferred.complete(Results.error(msg = "Failed to get download URL"))
                        }
                    }
                    .addOnFailureListener {
                        deferred.complete(Results.error(msg = it.message ?: "Error getting download URL"))
                    }
            }

        return withContext(Dispatchers.IO) {
            deferred.await()
        }
    }
}
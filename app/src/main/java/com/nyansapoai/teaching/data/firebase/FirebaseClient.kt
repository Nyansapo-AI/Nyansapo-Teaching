package com.nyansapoai.teaching.data.firebase

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.domain.models.school.LocalSchoolInfo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.runBlocking

class FirebaseClient(
    private val localDataSource: LocalDataSource,
    private val firebaseDb: FirebaseFirestore
) {
    private var localSchoolInfo: LocalSchoolInfo? = null
    private var isInitialized = false

    companion object {
        private const val ORGANIZATION_COLLECTION = "organization"
        private const val PROJECTS_COLLECTION = "projects"
        private const val SCHOOLS_COLLECTION = "schools"
        private const val TAG = "FirebaseClient"
    }

    suspend fun initialize() {
        if (isInitialized) return

        localDataSource.getSavedCurrentSchoolInfo()
            .catch { e ->
                Log.e(TAG, "Failed to get school info: ${e.message}")
                localSchoolInfo = null
            }
            .collect { data ->
                Log.d(TAG, "Local school fetched successfully: $data")
                localSchoolInfo = data
                isInitialized = true
            }
    }

    val schoolDocumentReference: DocumentReference get() {
        val orgId = localSchoolInfo?.organizationUid
        val projectId = localSchoolInfo?.projectUId
        val schoolId = localSchoolInfo?.schoolUId

        if (orgId.isNullOrEmpty() || projectId.isNullOrEmpty() || schoolId.isNullOrEmpty()) {
            Log.w(TAG, "Missing IDs: org=$orgId, project=$projectId, school=$schoolId")
        }

        return firebaseDb.collection(ORGANIZATION_COLLECTION)
            .document(orgId ?: "")
            .collection(PROJECTS_COLLECTION)
            .document(projectId ?: "")
            .collection(SCHOOLS_COLLECTION)
            .document(schoolId ?: "")
    }

    fun test(){
        Log.d(TAG, "document reference is : $schoolDocumentReference")
    }
}
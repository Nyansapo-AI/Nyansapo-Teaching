package com.nyansapoai.teaching.data.firebase.schools

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.nyansapoai.teaching.data.firebase.FirebaseClient
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.remote.school.SchoolRepository
import com.nyansapoai.teaching.domain.models.school.DetailedNyansapoSchool
import com.nyansapoai.teaching.domain.models.school.LocalSchoolInfo
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch

class SchoolRepositoryFirebaseImp(
    private val localDataSource: LocalDataSource,
    private val firebaseDb: FirebaseFirestore
): SchoolRepository {

    private var localSchoolInfo: LocalSchoolInfo? = null

    companion object {
        private const val ORGANIZATION_COLLECTION = "organization"
        private const val PROJECTS_COLLECTION = "projects"
        private const val SCHOOLS_COLLECTION = "schools"
        private const val TAG = "SchoolRepository"

    }

    suspend fun initialize() {
        localDataSource.getSavedCurrentSchoolInfo()
            .catch { e ->
                localSchoolInfo = null
            }
            .collect { data ->
                localSchoolInfo = data
            }
    }


    private suspend fun getSchoolDocumentReference(): DocumentReference {

        initialize()

        val orgId = localSchoolInfo?.organizationUid
        val projectId = localSchoolInfo?.projectUId
        val schoolId = localSchoolInfo?.schoolUId

        if (orgId.isNullOrEmpty() || projectId.isNullOrEmpty() || schoolId.isNullOrEmpty()) {
            throw IllegalArgumentException("Missing Firestore document IDs: orgId=$orgId, projectId=$projectId, schoolId=$schoolId")
        }

        return firebaseDb.collection(ORGANIZATION_COLLECTION)
            .document(orgId)
            .collection(PROJECTS_COLLECTION)
            .document(projectId)
            .collection(SCHOOLS_COLLECTION)
            .document(schoolId)
    }

    private val firebaseClient by lazy {
        FirebaseClient(localDataSource = localDataSource, firebaseDb = firebaseDb)
    }

    override suspend fun getSchoolInfo(organizationId: String, projectId: String, schoolId: String): Flow<Results<DetailedNyansapoSchool>> = callbackFlow{
//        val deferred = CompletableDeferred<Results<DetailedNyansapoSchool>>

        val snapshotListener = firebaseDb.collection(ORGANIZATION_COLLECTION)
            .document(organizationId)
            .collection(PROJECTS_COLLECTION)
            .document(projectId)
            .collection(SCHOOLS_COLLECTION)
            .document(schoolId)
            .addSnapshotListener { snapshot, error ->

                if (error != null){
                    trySend(Results.error(msg = error.message ?: "Something went wrong. Please try again"))
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()){
                    val schoolData = snapshot.toObject<DetailedNyansapoSchool>()
                    schoolData?.let { school ->
                        trySend(Results.success(data = school))
                    } ?: run {
                        trySend(Results.error("School Details not found"))
                    }
                }else{
                    trySend(Results.error(msg = "Unknown School identification"))
                }

            }

        awaitClose {
            snapshotListener.remove()
        }



        /*
        try {
            Log.d(TAG, "Getting school information")
//            val documentRef = getSchoolDocumentReference()
            Log.d(TAG, "Document reference: $documentRef")

            val documentSnapshot = firebaseDb.collection(ORGANIZATION_COLLECTION)
                .document(organizationId)
                .collection(PROJECTS_COLLECTION)
                .document(projectId)
                .collection(SCHOOLS_COLLECTION)
                .document(schoolId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){

                    }
                }

            val schoolData = documentSnapshot.toObject<DetailedNyansapoSchool>()

            if (schoolData != null) {
                Log.d(TAG, "School fetched successfully: $schoolData")
                Results.success(data = schoolData)
            } else {
                Log.w(TAG, "School data not found for document: ${documentRef.path}")
                Results.error("School data not found for the current school reference")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Error fetching school data", e)
            Results.error(msg = e.message ?: "Something went wrong. Please try again")
        }*/
    }
}
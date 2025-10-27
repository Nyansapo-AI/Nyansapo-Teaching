package com.nyansapoai.teaching.data.firebase.survey

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.nyansapoai.teaching.data.remote.survey.SurveyRepository
import com.nyansapoai.teaching.domain.models.school.LocalSchoolInfo
import com.nyansapoai.teaching.domain.models.survey.CreateHouseHoldInfo
import com.nyansapoai.teaching.domain.models.survey.HouseHoldInfo
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

class SurveyRepositoryFirebaseImp(
    private val firebaseDb: FirebaseFirestore,
) : SurveyRepository{

    companion object {
        private const val ORGANIZATION_COLLECTION = "organization"
        private const val PROJECTS_COLLECTION = "projects"
        private const val SCHOOLS_COLLECTION = "schools"
        private const val HOUSEHOLDS_COLLECTION = "households"
    }


    override fun getHouseholdSurveys(organizationId: String, projectId: String, schoolId: String): Flow<List<HouseHoldInfo>> = callbackFlow {

        if (projectId.isEmpty() || schoolId.isEmpty() || organizationId.isEmpty()){
            close(IllegalArgumentException("Invalid school information. Please sync school data and try again."))
            return@callbackFlow
        }


        val snapshotListener =
            firebaseDb.collection(ORGANIZATION_COLLECTION)
                .document(organizationId)
                .collection(PROJECTS_COLLECTION)
                .document(projectId)
                .collection(SCHOOLS_COLLECTION)
                .document(schoolId)
                .collection(HOUSEHOLDS_COLLECTION)
            .addSnapshotListener { snapshot, error ->
                if (error != null){
                    close(error)
                    return@addSnapshotListener
                }


                if (snapshot != null && !snapshot.isEmpty){
                    val households = snapshot.documents.mapNotNull { doc ->
                        doc.toObject<HouseHoldInfo>()
                    }
                    trySend(households)
                }else {
                    trySend(emptyList())
                }
            }

        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun submitHouseholdSurvey(
        createHouseHold: CreateHouseHoldInfo,
        localSchoolInfo: LocalSchoolInfo?
    ): Results<Unit> {
        if (localSchoolInfo == null ||
            localSchoolInfo.schoolUId.isEmpty() ||
            localSchoolInfo.projectUId.isEmpty() ||
            localSchoolInfo.organizationUid.isEmpty()) {
            Log.e("SurveyRepositoryFirebaseImp", "submitHouseholdSurvey: Invalid school information. Please sync school data and try again.")
            Log.e("SurveyRepositoryFirebaseImp", "OrganizationId: ${localSchoolInfo?.organizationUid}, ProjectId: ${localSchoolInfo?.projectUId}, SchoolId: ${localSchoolInfo?.schoolUId}")
            return Results.error(msg = "Invalid school information. Please sync school data and try again.")
        }

        val deferred = CompletableDeferred<Results<Unit>>()

        firebaseDb.collection(ORGANIZATION_COLLECTION)
            .document(localSchoolInfo.organizationUid)
            .collection(PROJECTS_COLLECTION)
            .document(localSchoolInfo.projectUId)
            .collection(SCHOOLS_COLLECTION)
            .document(localSchoolInfo.schoolUId)
            .collection(HOUSEHOLDS_COLLECTION)
            .document(createHouseHold.id)
            .set(createHouseHold)
            .addOnSuccessListener {
                Log.d("SurveyRepositoryFirebaseImp", "submitHouseholdSurvey: Success")
                deferred.complete(Results.success(Unit))

            }
            .addOnFailureListener { exception ->
                Log.e("SurveyRepositoryFirebaseImp", "submitHouseholdSurvey: Failure", exception)
                deferred.complete(Results.error(msg = exception.message ?: "Unknown error"))
            }

        return withContext(Dispatchers.IO){
            deferred.await()
        }

    }

    override fun getHouseholdSurveyById(
        organizationId: String,
        projectId: String,
        schoolId: String,
        id: String
    ): Flow<HouseHoldInfo?>  = callbackFlow {

        if (projectId.isEmpty() || schoolId.isEmpty() || organizationId.isEmpty()){
            close(IllegalArgumentException("Invalid school information. Please sync school data and try again."))
            return@callbackFlow
        }

        val snapshotListener =
            firebaseDb.collection(ORGANIZATION_COLLECTION)
                .document(organizationId)
                .collection(PROJECTS_COLLECTION)
                .document(projectId)
                .collection(SCHOOLS_COLLECTION)
                .document(schoolId)
                .collection(HOUSEHOLDS_COLLECTION)
            .document(id)
            .addSnapshotListener { snapshot, error ->
                if (error != null){
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()){
                    val household = snapshot.toObject<HouseHoldInfo>()
                    trySend(household)
                }else {
                    trySend(null)
                }
            }

        awaitClose {
            snapshotListener.remove()
        }
    }
}
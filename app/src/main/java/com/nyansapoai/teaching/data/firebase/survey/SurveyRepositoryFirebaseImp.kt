package com.nyansapoai.teaching.data.firebase.survey

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.nyansapoai.teaching.data.remote.survey.SurveyRepository
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

    private val householdCollection = "households"

    override fun getHouseholdSurveys(village: String): Flow<List<HouseHoldInfo>> = callbackFlow {
        val snapshotListener = firebaseDb.collection(householdCollection)
            .whereEqualTo("village", village)
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

    override suspend fun submitHouseholdSurvey(createHouseHold: CreateHouseHoldInfo): Results<Unit> {
        val deferred = CompletableDeferred<Results<Unit>>()

        firebaseDb.collection(householdCollection)
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

    override fun getHouseholdSurveyById(id: String): Flow<HouseHoldInfo?>  = callbackFlow {
        val snapshotListener = firebaseDb.collection(householdCollection)
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
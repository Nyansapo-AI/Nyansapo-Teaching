package com.nyansapoai.teaching.data.firebase.assessment

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.domain.models.assessments.Assessment
import com.nyansapoai.teaching.domain.models.assessments.AssignedStudent
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AssessmentRepositoryFirebaseImp(
    private val firebaseDb: FirebaseFirestore
): AssessmentRepository {

    private val assessmentCollection = "assessments"

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createAssessment(
        name: String,
        type: String,
        startLevel: String,
        assessmentNumber: Int,
        assignedStudents: List<AssignedStudent>
    ): Results<Unit> {

        val deferred = CompletableDeferred<Results<Unit>>()

        val newAssessment = Assessment(
            id = Uuid.random().toString(),
            created_at = Clock.System.now().toString(),
            name = name,
            type = type,
            start_level = startLevel,
            assessmentNumber = assessmentNumber,
            assigned_students = assignedStudents
        )

        firebaseDb.collection(assessmentCollection)
            .add(newAssessment)
            .addOnSuccessListener {
                Log.d("create assessment", "Assessment created successfully")
                deferred.complete(Results.success(data = Unit))
            }
            .addOnFailureListener { e ->
                Log.d("create assessment", "Error creating assessment: ${e.message}")
                deferred.complete(Results.error(msg = e.message ?: "Error creating assessment"))
            }

        return withContext(Dispatchers.IO){
            deferred.await()
        }
    }

    override suspend fun getAssessments(): Flow<List<Assessment>> {
        TODO("Not yet implemented")
    }

}
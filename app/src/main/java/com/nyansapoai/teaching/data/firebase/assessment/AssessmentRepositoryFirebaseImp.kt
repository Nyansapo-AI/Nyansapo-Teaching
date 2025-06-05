package com.nyansapoai.teaching.data.firebase.assessment

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.domain.models.assessments.Assessment
import com.nyansapoai.teaching.domain.models.assessments.AssignedStudent
import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyWordProblem
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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
            .document(newAssessment.id)
            .set(newAssessment)
            .addOnSuccessListener {
                deferred.complete(Results.success(data = Unit))
            }
            .addOnFailureListener { e ->
                Log.d("create assessment", "Error creating assessment: ${e.message}")
            }

        return withContext(Dispatchers.IO){
            deferred.await()
        }
    }

    override suspend fun getAssessments(): Flow<List<Assessment>> = callbackFlow {
        val snapshotListener = firebaseDb.collection(assessmentCollection)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("getAssessments", "Listen failed.", e)
                    close(e)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val assessments = snapshot.documents.mapNotNull { it.toObject<Assessment>() }
                    trySend(assessments)
                } else {
                    trySend(emptyList())
                }
            }

        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun getAssessmentById(assessmentId: String): Flow<Results<Assessment>> = callbackFlow {

        val snapshotListener = firebaseDb.collection(assessmentCollection)
            .document(assessmentId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("getAssessmentById", "Listen failed.", e)
                    trySend(Results.error(msg = e.message ?: "Something went wrong"))
                    close(e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val assessment = snapshot.toObject<Assessment>()
                    if (assessment != null) {
                        trySend(Results.success(data = assessment))
                    } else {
                        trySend(Results.error(msg = "Assessment not found"))
                    }
                } else {
                    trySend(Results.error(msg = "Unknown assessment ID"))
                }
            }

        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun assessNumeracyCountAndMatch(
        assessmentId: String,
        studentID: String,
        countAndMatchList: List<CountMatch>
    ): Results<String> {
        val deferred = CompletableDeferred<Results<String>>()

        firebaseDb.collection(assessmentCollection)
            .document(assessmentId)
            .collection("assessments-results")
            .document(assessmentId+"_$studentID")
            .set(
                mapOf(
                    "assessmentId" to assessmentId,
                    "student_id" to studentID,
                    "numeracy_results.count_and_match" to countAndMatchList
                )
            )
            .addOnSuccessListener {
                Log.d("AssessmentRepositoryFirebaseImp", "Assessment submitted count and match successfully")
                deferred.complete(Results.success(data = "Assessment submitted successfully"))
            }
            .addOnFailureListener {
                Log.d("AssessmentRepositoryFirebaseImp", "Failed to submit count and match assessment: ${it.message}")
                deferred.complete(Results.error(msg = "Failed to submit assessment: ${it.message}"))
            }


        return withContext(Dispatchers.IO) {
            deferred.await()
        }
    }

    override suspend fun assessNumeracyNumberRecognition(
        assessmentId: String,
        studentID: String,
        numberRecognitionList: List<String>
    ): Results<String> {
        val deferred = CompletableDeferred<Results<String>>()

        firebaseDb.collection(assessmentCollection)
            .document(assessmentId)
            .collection("assessments-results")
            .document(assessmentId+"_$studentID")
            .set(
                mapOf(
                    "assessmentId" to assessmentId,
                    "student_id" to studentID,
                    "numeracy_results" to mapOf(
                        "number_recognition" to numberRecognitionList
                    )
                )
            )
            .addOnSuccessListener {
                deferred.complete(Results.success(data = "Assessment submitted successfully"))
            }
            .addOnFailureListener {
                deferred.complete(Results.error(msg = "Failed to submit assessment: ${it.message}"))
            }

        return withContext(Dispatchers.IO) {
            deferred.await()
        }
    }

    override suspend fun assessNumeracyArithmeticOperations(
        assessmentId: String,
        studentID: String,
        arithmeticOperations: List<NumeracyArithmeticOperation>
    ): Results<String> {
        val deferred = CompletableDeferred<Results<String>>()

        firebaseDb.collection(assessmentCollection)
            .document(assessmentId)
            .collection("assessments-results")
            .document(assessmentId+"_$studentID")
            .set(
                mapOf(
                    "assessmentId" to assessmentId,
                    "student_id" to studentID,
                    "numeracy_results" to mapOf(
                        "number_operations" to arithmeticOperations
                    )
                )
            )
            .addOnSuccessListener {
                Log.d("AssessmentRepositoryFirebaseImp", "Assessment submitted successfully")
                deferred.complete(Results.success(data = "Assessment submitted successfully"))
            }
            .addOnFailureListener {
                Log.d("AssessmentRepositoryFirebaseImp", "Failed to submit assessment: ${it.message}")
                deferred.complete(Results.error(msg = "Failed to submit assessment: ${it.message}"))
            }

        return withContext(Dispatchers.IO) {
            deferred.await()
        }
    }

    override suspend fun assessNumeracyWordProblem(
        assessmentId: String,
        studentID: String,
        wordProblem: NumeracyWordProblem
    ) {

        val deferred = CompletableDeferred<Results<String>>()

        firebaseDb.collection(assessmentCollection)
            .document(assessmentId)
            .collection("assessments-results")
            .document(assessmentId+"_$studentID")
            .set(
                mapOf(
                    "assessmentId" to assessmentId,
                    "student_id" to studentID,
                    "numeracy_results" to mapOf(
                        "word_problem" to wordProblem
                    )
                )
            )
            .addOnSuccessListener {
                deferred.complete(Results.success(data = "Assessment submitted successfully"))
            }
            .addOnFailureListener {
                deferred.complete(Results.error(msg = "Failed to submit assessment: ${it.message}"))
            }


        return withContext(Dispatchers.IO) {
            deferred.await()
        }
    }

}
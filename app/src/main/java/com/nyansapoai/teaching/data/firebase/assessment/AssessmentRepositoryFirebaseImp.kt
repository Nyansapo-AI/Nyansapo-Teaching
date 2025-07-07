package com.nyansapoai.teaching.data.firebase.assessment

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.domain.models.assessments.Assessment
import com.nyansapoai.teaching.domain.models.assessments.AssignedStudent
import com.nyansapoai.teaching.domain.models.assessments.literacy.MultipleChoicesResult
import com.nyansapoai.teaching.domain.models.assessments.literacy.ReadingAssessmentMetadata
import com.nyansapoai.teaching.domain.models.assessments.literacy.ReadingAssessmentResult
import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyWordProblem
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlin.text.set
import kotlin.toString
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

        // Create the main assessment document
        firebaseDb.collection(assessmentCollection)
            .document(newAssessment.id)
            .set(newAssessment)
            .addOnSuccessListener {
                // Create assessment result documents for each assigned student
                val batch = firebaseDb.batch()

                assignedStudents.forEach { student ->
                    val resultDocRef = firebaseDb.collection(assessmentCollection)
                        .document(newAssessment.id)
                        .collection("assessments-results")
                        .document("${newAssessment.id}_${student.student_id}")

                    batch.set(resultDocRef, mapOf(
                        "assessmentId" to newAssessment.id,
                        "student_id" to student.student_id,
                        type to null
                    ))
                }

                // Execute batch write for all student documents
                batch.commit()
                    .addOnSuccessListener {
                        deferred.complete(Results.success(data = Unit))
                    }
                    .addOnFailureListener { e ->
                        Log.d("create assessment", "Error creating student result documents: ${e.message}")
                        deferred.complete(Results.error(msg = "Failed to create student result documents: ${e.message}"))
                    }
            }
            .addOnFailureListener { e ->
                Log.d("create assessment", "Error creating assessment: ${e.message}")
                deferred.complete(Results.error(msg = "Failed to create assessment: ${e.message}"))
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
                    "numeracy_results" to mapOf(
                        "count_and_match" to countAndMatchList
                    )
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

    override suspend fun markLiteracyAssessmentAsComplete(
        assessmentId: String,
        studentId: String
    ): Results<String> {
        val deferred = CompletableDeferred<Results<String>>()

        val documentRef = firebaseDb.collection(assessmentCollection)
            .document(assessmentId)
            .collection("assessments-results")
            .document("${assessmentId}_$studentId")
            .update("completed_assessment", true)
            .addOnCompleteListener {
                deferred.complete(Results.success(data = "Assessment is completed successful"))
            }
            .addOnFailureListener {
                deferred.complete(Results.error(msg = "Assessment could not be completed"))
            }


        return withContext(Dispatchers.IO) {
            deferred.await()
        }

    }

    override suspend fun assessReadingAssessment(
        assessmentId: String,
        studentID: String,
        readingAssessmentResults: List<ReadingAssessmentResult>
    ): Results<String> {
        val deferred = CompletableDeferred<Results<String>>()
        val documentRef = firebaseDb.collection(assessmentCollection)
            .document(assessmentId)
            .collection("assessments-results")
            .document("${assessmentId}_$studentID")

        firebaseDb.runTransaction { transaction ->
            val snapshot = transaction.get(documentRef)
            val existingResults = mutableListOf<ReadingAssessmentResult>()

            // Extract existing reading results if they exist
            if (snapshot.exists()) {
                val literacyResults = snapshot.get("literacy_results") as? Map<String, Any>
                val readingResults = literacyResults?.get("reading_results") as? List<Map<String, Any>>

                readingResults?.forEach { resultMap ->
                    try {
                        val result = convertMapToReadingAssessmentResult(resultMap)
                        existingResults.add(result)
                    } catch (e: Exception) {
                        // Handle conversion error if needed
                    }
                }
            }

            // Add new results to existing ones
            existingResults.addAll(readingAssessmentResults)

            // Update the document with merged results
            transaction.set(
                documentRef,
                mapOf(
                    "literacy_results" to mapOf(
                        "reading_results" to existingResults
                    )
                ),
                SetOptions.merge()
            )
        }
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

    override suspend fun assessMultipleChoiceQuestions(
        assessmentId: String,
        studentID: String,
        multipleChoiceQuestions: List<MultipleChoicesResult>
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
                    "literacy_results" to mapOf(
                        "multiple_choice_questions" to multipleChoiceQuestions
                    )
                ),
                SetOptions.merge()
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



    private fun checkIfAssessmentExists(assessmentId: String): Flow<Boolean> = callbackFlow {
        val snapshotListener = firebaseDb.collection(assessmentCollection)
            .document(assessmentId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("checkIfAssessmentExists", "Listen failed.", e)
                    trySend(false)
                    close(e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    trySend(true)
                } else {
                    trySend(false)
                }
            }

        awaitClose {
            snapshotListener.remove()
        }
    }

    private suspend fun createAssessmentResultDocument(
        assessmentId: String,
        studentID: String,
        onAction: () -> Unit
    ): Results<String> {
        val deferred = CompletableDeferred<Results<String>>()

        val documentPath = "$assessmentCollection/$assessmentId/assessments-results/${assessmentId}_$studentID"


        when(
            checkIfAssessmentExists(assessmentId = "${assessmentId}_$studentID").single()
        ){
            true -> {
                onAction.invoke()
            }
            false -> {
                firebaseDb.collection(documentPath)
                    .document()
                    .set(
                        mapOf(
                            "assessmentId" to assessmentId,
                            "student_id" to studentID,
                            "completed_assessment" to false
                        )
                    )
                    .addOnSuccessListener {
//                        trySend("Assessment result document created successfully")

                        deferred.complete(Results.success("Assessment result document created successfully"))

                        onAction.invoke()
                    }
                    .addOnFailureListener { e ->
                        Log.w("createAssessmentResultDocument", "Error creating document: ${e.message}")
//                        trySend("Failed to create assessment result document: ${e.message}")

                        deferred.complete(Results.error("Failed to created assessment"))
                    }
            }
        }


        return withContext(Dispatchers.IO) {
            deferred.await()
        }
    }

    private fun convertMapToReadingAssessmentResult(map: Map<String, Any>): ReadingAssessmentResult {
        val type = map["type"] as? String ?: ""
        val content = map["content"] as? String ?: ""

        // Handle metadata conversion
        val metadata = (map["metadata"] as? Map<String, Any>)?.let { metadataMap ->
            ReadingAssessmentMetadata(
                audio_url = metadataMap["audio_url"] as? String ?: "",
                passed = metadataMap["passed"] as? Boolean ?: false,
                transcript = metadataMap["transcript"] as? String ?: ""
            )
        }

        return ReadingAssessmentResult(
            type = type,
            content = content,
            metadata = metadata
        )
    }

    private fun convertReadingAssessmentResultToMap(result: ReadingAssessmentResult): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "type" to result.type,
            "content" to result.content
        )

        result.metadata?.let { metadata ->
            map["metadata"] = mapOf(
                "audio_url" to metadata.audio_url,
                "passed" to metadata.passed,
                "transcript" to metadata.transcript
            )
        }

        return map
    }




}
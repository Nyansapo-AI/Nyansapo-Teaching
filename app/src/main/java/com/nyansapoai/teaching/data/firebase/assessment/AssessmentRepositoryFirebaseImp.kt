package com.nyansapoai.teaching.data.firebase.assessment

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.domain.models.assessments.Assessment
import com.nyansapoai.teaching.domain.models.assessments.CompletedAssessment
import com.nyansapoai.teaching.domain.models.assessments.literacy.LiteracyAssessmentResults
import com.nyansapoai.teaching.domain.models.assessments.literacy.MultipleChoicesResult
import com.nyansapoai.teaching.domain.models.assessments.literacy.ReadingAssessmentMetadata
import com.nyansapoai.teaching.domain.models.assessments.literacy.ReadingAssessmentResult
import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyWordProblem
import com.nyansapoai.teaching.domain.models.students.NyansapoStudent
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlin.text.get
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AssessmentRepositoryFirebaseImp(
    private val firebaseDb: FirebaseFirestore,
//    private val assessmentDocumentReference: DocumentReference
): AssessmentRepository {

    private val assessmentCollection = "assessments"
    private val assessmentResultsCollection = "assessments-results"

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createAssessment(
        name: String,
        type: String,
        startLevel: String,
        assessmentNumber: Int,
        assignedStudents: List<NyansapoStudent>,
        schoolId: String,
        organizationId: String,
        projectId: String
    ): Results<Unit> {
        val deferred = CompletableDeferred<Results<Unit>>()


        if (schoolId.isEmpty()){
            deferred.complete(Results.error(msg = "School ID cannot be null or empty"))
        }



        val newAssessment = Assessment(
            id = Uuid.random().toString(),
            created_at = Clock.System.now().toString(),
            name = name,
            type = type,
            start_level = startLevel,
            assessmentNumber = assessmentNumber,
            assigned_students = assignedStudents,
            school_id = schoolId,
            organization_id = organizationId,
            project_id = projectId
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
                        .document("${newAssessment.id}_${student.id}")

                    batch.set(resultDocRef, mapOf(
                        "assessmentId" to newAssessment.id,
                        "school_id" to schoolId,
                        "student_id" to student.id,
                        "student_name" to student.name,
                        "student_first_name" to student.first_name,
                        "student_last_name" to student.last_name,
                        "student_grade" to student.grade,
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

    override suspend fun getAssessments(schoolId: String): Flow<List<Assessment>> = callbackFlow {
        val snapshotListener = firebaseDb.collection(assessmentCollection)
            .whereEqualTo("school_id", schoolId)
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

    override fun getAssessmentById(assessmentId: String): Flow<Results<Assessment>> = callbackFlow {

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
    ): Results<String> = withContext(Dispatchers.IO) {
        val deferred = CompletableDeferred<Results<String>>()
        val docRef = firebaseDb.collection(assessmentCollection)
            .document(assessmentId)
            .collection("assessments-results")
            .document("${assessmentId}_$studentID")

        docRef.set(
            mapOf(
                "assessmentId" to assessmentId,
                "student_id" to studentID,
                "numeracy_results" to mapOf(
                    "count_and_match" to countAndMatchList
                )
            ),
            SetOptions.merge()
        )
            .addOnSuccessListener {
                Log.d("AssessmentRepositoryFirebaseImp", "Assessment submitted count and match successfully")
                deferred.complete(Results.success(data = "Assessment submitted successfully"))
            }
            .addOnFailureListener {
                Log.d("AssessmentRepositoryFirebaseImp", "Failed to submit count and match assessment: ${it.message}")
                deferred.complete(Results.error(msg = "Failed to submit assessment: ${it.message}"))
            }

        deferred.await()
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
        wordProblemList: List<NumeracyWordProblem>
    ): Results<String> {

        val deferred = CompletableDeferred<Results<String>>()

        firebaseDb.collection(assessmentCollection)
            .document(assessmentId)
            .collection("assessments-results")
            .document(assessmentId+"_$studentID")
            .update(
                mapOf(
                    "assessmentId" to assessmentId,
                    "student_id" to studentID,
                    "numeracy_results" to mapOf(
                        "word_problems" to wordProblemList
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
        val documentId = "${assessmentId}_$studentId"
        val documentRef = firebaseDb.collection(assessmentCollection)
            .document(assessmentId)
            .collection("assessments-results")
            .document(documentId)

        Log.d("AssessmentRepo", "Attempting to mark assessment $assessmentId complete for student $studentId")

        firebaseDb.runTransaction { transaction ->
            val snapshot = transaction.get(documentRef)

            if (!snapshot.exists()) {
                Log.w("AssessmentRepo", "Document $documentId does not exist")
                throw Exception("Assessment not found")
            }

            val literacyResults = snapshot.get("literacy_results") as? Map<String, Any>
            if (literacyResults == null) {
                Log.w("AssessmentRepo", "No literacy_results found in document $documentId")
                throw Exception("Literacy assessment data not found")
            }

            // Check both required components with detailed validation
            val readingResults = literacyResults["reading_results"] as? List<*>
            if (readingResults.isNullOrEmpty()) {
                Log.w("AssessmentRepo", "Reading results missing or empty in $documentId")
                throw Exception("Reading assessment section is incomplete")
            }

            val multipleChoiceQuestions = literacyResults["multiple_choice_questions"] as? List<*>
            if (multipleChoiceQuestions.isNullOrEmpty()) {
                Log.w("AssessmentRepo", "Multiple choice questions missing or empty in $documentId")
                throw Exception("Multiple choice section is incomplete")
            }

            Log.d("AssessmentRepo", "Validation successful, marking assessment as complete")

            // Update completed status and completion timestamp
            transaction.update(
                documentRef,
                mapOf(
                    "completed_assessment" to true,
//                    "completed_at" to FieldValue.serverTimestamp()
                )
            )
        }
            .addOnSuccessListener {
                Log.d("AssessmentRepo", "Successfully marked assessment $documentId as complete")
                deferred.complete(Results.success(data = "Assessment has been marked as complete"))
            }
            .addOnFailureListener { e ->
                Log.e("AssessmentRepo", "Failed to mark assessment complete: ${e.message}", e)
                val errorMsg = when {
                    e.message?.contains("Reading assessment") == true -> "Reading assessment section must be completed first"
                    e.message?.contains("Multiple choice") == true -> "Multiple choice section must be completed first"
                    e.message?.contains("not found") == true -> "Assessment not found"
                    else -> "Unable to complete assessment: ${e.message}"
                }
                deferred.complete(Results.error(msg = errorMsg))
            }

        return withContext(Dispatchers.IO) {
            deferred.await()
        }
    }

    override fun getCompletedAssessments(assessmentId: String): Flow<Results<List<CompletedAssessment>>> = callbackFlow {
        // Validate assessmentId first to prevent malformed paths
        if (assessmentId.isBlank()) {
            trySend(Results.error(msg = "Invalid assessment ID provided"))
            close()
            return@callbackFlow
        }

        try {
            // Log the path for debugging
            val path = "$assessmentCollection/$assessmentId/$assessmentResultsCollection"
            Log.d("AssessmentRepo", "Fetching completed assessments from: $path")

            val snapshotListener = firebaseDb.collection(assessmentCollection)
                .document(assessmentId)
                .collection(assessmentResultsCollection)
                .whereEqualTo("completed_assessment", true)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("AssessmentRepo", "Error fetching completed assessments: ${error.message}", error)
                        trySend(Results.error(msg = error.message ?: "Something went wrong"))
                        close(error)
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val completedAssessments = snapshot.documents.mapNotNull { documentSnapshot ->
                            try {
                                documentSnapshot.toObject<CompletedAssessment>()
                            } catch (e: Exception) {
                                Log.e("AssessmentRepo", "Error parsing assessment: ${e.message}")
                                null
                            }
                        }
                        trySend(Results.success(data = completedAssessments))
                    } else {
                        trySend(Results.error(msg = "No assessment data found"))
                    }
                }

            awaitClose {
                snapshotListener.remove()
            }
        } catch (e: Exception) {
            Log.e("AssessmentRepo", "Exception in getCompletedAssessments: ${e.message}", e)
            trySend(Results.error(msg = "Error accessing assessments: ${e.message}"))
            close(e)
        }
    }

    override fun fetchLiteracyAssessmentResults(
        assessmentId: String,
        studentId: String
    ): Flow<Results<LiteracyAssessmentResults>> {
        return callbackFlow {
            if (assessmentId.isBlank() || studentId.isBlank()) {
                Log.w("AssessmentRepo", "Invalid assessment ID or student ID provided")
                trySend(Results.error(msg = "Invalid assessment ID or student ID provided"))
                close()
                return@callbackFlow
            }

            try {
                val documentId = "${assessmentId}_$studentId"
                val path = "$assessmentCollection/$assessmentId/$assessmentResultsCollection/$documentId"
                Log.d("AssessmentRepo", "Fetching literacy results from: $path")

                val snapshotListener = firebaseDb.collection(assessmentCollection)
                    .document(assessmentId)
                    .collection(assessmentResultsCollection)
                    .document(documentId)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            Log.e("AssessmentRepo", "Error fetching literacy results: ${error.message}", error)
                            trySend(Results.error(msg = error.message ?: "Something went wrong"))
                            close(error)
                            return@addSnapshotListener
                        }

                        if (snapshot != null && snapshot.exists()) {
                            try {

                                val results = snapshot.toObject<LiteracyAssessmentResults>()
                                if (results != null) {
                                    Log.d("AssessmentRepo", "Literacy results fetched successfully, size of reading results: ${snapshot.data}")
                                    Log.d("AssessmentRepo", "Literacy results fetched successfully, size of multiple choice questions: ${results}")

                                    trySend(Results.success(data = results))
                                } else {
                                    Log.w("AssessmentRepo", "No literacy assessment data found")

                                    trySend(Results.error(msg = "No literacy assessment data found"))
                                }
                            } catch (e: Exception) {
                                Log.e("AssessmentRepo", "Error parsing literacy results: ${e.message}", e)
                                trySend(Results.error(msg = "Error parsing data: ${e.message}"))
                            }
                        } else {
                            Log.w("AssessmentRepo", "Literacy assessment not found for ID: $documentId")
                            trySend(Results.error(msg = "Literacy assessment not found"))
                        }
                    }

                awaitClose {
                    snapshotListener.remove()
                }
            } catch (e: Exception) {
                Log.e("AssessmentRepo", "Exception in fetchLiteracyAssessmentResults: ${e.message}", e)
                trySend(Results.error(msg = "Error accessing literacy results: ${e.message}"))
                close(e)
            }
        }
    }

    override suspend fun markAssessmentDone(
        assessmentId: String,
        studentId: String
    ): Results<String> {
        val deferred = CompletableDeferred<Results<String>>()

        firebaseDb.collection(assessmentCollection)
            .document(assessmentId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val assessment = documentSnapshot.toObject<Assessment>()
                    assessment?.let {
                        // Update assigned_students list with has_done=true for the specific student
                        val updatedStudents = it.assigned_students.map { student ->
                            if (student.id == studentId) {
                                // Create updated student with has_done=true
                                student.copy(has_done = true)
                            } else {
                                student
                            }
                        }

                        // Update the document with the modified student list
                        firebaseDb.collection(assessmentCollection)
                            .document(assessmentId)
                            .update("assigned_students", updatedStudents)
                            .addOnSuccessListener {
                                deferred.complete(Results.success(data = "Assessment marked as done"))
                            }
                            .addOnFailureListener { e ->
                                deferred.complete(Results.error(msg = "Failed to mark assessment as done: ${e.message}"))
                            }
                    } ?: deferred.complete(Results.error(msg = "Assessment not found"))
                } else {
                    deferred.complete(Results.error(msg = "Assessment not found"))
                }
            }
            .addOnFailureListener { e ->
                deferred.complete(Results.error(msg = "Failed to retrieve assessment: ${e.message}"))
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

    override suspend fun addReadingAssessmentResult(
        assessmentId: String,
        studentID: String,
        readingAssessment: ReadingAssessmentResult
    ): Results<String> {
        TODO("Not yet implemented")
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
//                passed = metadataMap["passed"] as? Boolean ?: false,
//                transcript = metadataMap["transcript"] as? String ?: ""
            )
        }

        return ReadingAssessmentResult(
            type = type,
            content = content,
            metadata = metadata
        )
    }

}
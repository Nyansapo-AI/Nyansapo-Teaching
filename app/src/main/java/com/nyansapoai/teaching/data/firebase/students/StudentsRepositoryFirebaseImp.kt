package com.nyansapoai.teaching.data.firebase.students

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.nyansapoai.teaching.data.remote.students.StudentsRepository
import com.nyansapoai.teaching.domain.models.students.NyansapoStudent
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.text.get

class StudentsRepositoryFirebaseImp(
    private val firebaseDb: FirebaseFirestore,
): StudentsRepository {

    companion object {
        private const val ORGANIZATION_COLLECTION = "organization"
        private const val PROJECTS_COLLECTION = "projects"
        private const val SCHOOLS_COLLECTION = "schools"
        private const val STUDENTS_COLLECTION = "students"
//        private const val TAG = "SchoolRepository"

    }

    override suspend fun getSchoolStudents(
        organizationId: String,
        projectId: String,
        schoolId: String,
        studentClass: Int?
    ): Flow<Results<List<NyansapoStudent>>> = callbackFlow{

        Log.d("SchoolRepository", "getSchoolStudents: org $organizationId project $projectId school $schoolId class $studentClass")



        val documentRef = studentClass?.let {
            firebaseDb.collection(ORGANIZATION_COLLECTION)
                .document(organizationId)
                .collection(PROJECTS_COLLECTION)
                .document(projectId)
                .collection(SCHOOLS_COLLECTION)
                .document(schoolId)
                .collection(STUDENTS_COLLECTION)
                .whereEqualTo("grade", studentClass)
        } ?: run {
            firebaseDb.collection(ORGANIZATION_COLLECTION)
                .document(organizationId)
                .collection(PROJECTS_COLLECTION)
                .document(projectId)
                .collection(SCHOOLS_COLLECTION)
                .document(schoolId)
                .collection(STUDENTS_COLLECTION)
        }

        val snapshotListener = documentRef
            .addSnapshotListener { snapshot, error ->

                Log.d("SchoolRepository", "getSchoolStudents: snapshot ${snapshot?.documents} error $error")

                if (error != null){
                    trySend(Results.error(msg = error.message ?: "Something went wrong. Please try again"))
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null){
                    val data = snapshot.documents.mapNotNull { documentSnapshot ->

                        val gradeValue = when (val g = documentSnapshot.get("grade")) {
                            is Number -> g.toInt()
                            is String -> g.toIntOrNull()
                            else -> null
                        }

                        NyansapoStudent(
                            id = documentSnapshot.id,
                            baseline = documentSnapshot.getString("baseline") ?: "",
                            grade = gradeValue,
                            group = documentSnapshot.getString("group") ?: "",
                            name = documentSnapshot.getString("name") ?: "",
                            first_name = documentSnapshot.getString("first_name") ?: "",
                            last_name = documentSnapshot.getString("last_name") ?: "",
                            sex = documentSnapshot.getString("sex") ?: "",
                            isLinked = documentSnapshot.getBoolean("isLinked") ?: false
                        )
                    }

                    Log.d("SchoolRepository", "getSchoolStudents: data $data")
                    trySend(Results.success(data = data))
                }else{
                    Log.d("SchoolRepository", "getSchoolStudents: snapshot is null")
                    trySend(Results.error(msg = "Unknown Students identification"))
                }
            }

        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun updateStudentLinkStatus(
        organizationId: String,
        projectId: String,
        schoolId: String,
        studentId: String,
        firstName: String,
        lastName: String,
        isLinked: Boolean
    ): Results<Unit> {
        return try {
            val docRef = firebaseDb.collection(ORGANIZATION_COLLECTION)
                .document(organizationId)
                .collection(PROJECTS_COLLECTION)
                .document(projectId)
                .collection(SCHOOLS_COLLECTION)
                .document(schoolId)
                .collection(STUDENTS_COLLECTION)
                .document(studentId)

            val snapshot = docRef.get().await()
            if (!snapshot.exists()) {
                return Results.error(msg = "Student not found")
            }

            docRef.update(
                mapOf(
                    "first_name" to firstName,
                    "last_name" to lastName,
                    "isLinked" to isLinked
                )
            ).await()

            Results.success(Unit)
        } catch (e: Exception) {
            Results.error(msg = e.message ?: "Failed to update student link status")
        }
    }
}
package com.nyansapoai.teaching.data.firebase.students

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.nyansapoai.teaching.data.remote.students.StudentsRepository
import com.nyansapoai.teaching.domain.models.students.NyansapoStudent
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

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
                if (error != null){
                    trySend(Results.error(msg = error.message ?: "Something went wrong. Please try again"))
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null){
                    val data = snapshot.documents.mapNotNull { documentSnapshot ->
                        val student = documentSnapshot.toObject<NyansapoStudent>()

                        NyansapoStudent(
                            id = documentSnapshot.id,
                            baseline = student?.baseline ?: "",
                            grade = student?.grade,
                            createdAt = student?.createdAt ?: "",
                            group = student?.group ?: "",
                            lastUpdated = student?.lastUpdated ?: "",
                            name = student?.name ?: "",
                            first_name = student?.first_name ?: "",
                            last_name = student?.last_name ?: "",
                            sex = student?.sex ?: ""
                        )
                    }

                    trySend(Results.success(data = data))
                }else{
                    trySend(Results.error(msg = "Unknown Students identification"))
                }
            }

        awaitClose {
            snapshotListener.remove()
        }
    }
}
package com.nyansapoai.teaching.data.firebase.attendance

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.nyansapoai.teaching.data.remote.attendance.AttendanceRepository
import com.nyansapoai.teaching.domain.models.attendance.AttendanceRecord
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseAttendanceRepositoryImp(
    private val firebaseDb: FirebaseFirestore
): AttendanceRepository {


    companion object {
        private const val ORGANIZATION_COLLECTION = "organization"
        private const val PROJECTS_COLLECTION = "projects"
        private const val SCHOOLS_COLLECTION = "schools"
        private const val ATTENDANCE_COLLECTION = "attendance"
    }

    override suspend fun getAttendanceDataByDate(
        date: String,
        organizationId: String,
        projectId: String,
        schoolId: String
    ): Flow<Results<AttendanceRecord?>> = callbackFlow {

        if (
            date.isEmpty() ||
            organizationId.isEmpty() ||
            projectId.isEmpty() ||
            schoolId.isEmpty()
        ) {
            trySend(Results.error("Invalid date or organization id or project id or school id"))
            close(Throwable(message = "Invalid date or organization id or project id or school id"))
            return@callbackFlow
        }

        Log.d("AttendanceRepositoryImp", "getAttendanceData: $date, $organizationId, $projectId, $schoolId")

        val documentRef = firebaseDb.collection(ORGANIZATION_COLLECTION)
            .document(organizationId)
            .collection(PROJECTS_COLLECTION)
            .document(projectId)
            .collection(SCHOOLS_COLLECTION)
            .document(schoolId)
            .collection(ATTENDANCE_COLLECTION)
            .document(date)

        val snapshotListener = documentRef
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Results.error(error.message ?: "Unknown error"))
                    close(error)
                    Log.d("AttendanceRepositoryImp", "getAttendanceData: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("AttendanceRepositoryImp", "getAttendanceData: $snapshot")
                    val attendanceRecord = snapshot.toObject<AttendanceRecord>()
                    trySend(Results.success(attendanceRecord))
                } else {
                    Log.d("AttendanceRepositoryImp", "getAttendanceData: $snapshot")
                    trySend(Results.success(null))
                }
            }

        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun submitAttendanceData(
        attendanceRecord: AttendanceRecord,
        organizationId: String,
        projectId: String,
        schoolId: String
    ): Results<Unit> {

        if(organizationId.isEmpty() ||
            projectId.isEmpty() ||
            schoolId.isEmpty())
        {
            return Results.error("Invalid organization id or project id or school id")
        }


        return try {
            firebaseDb.collection(ORGANIZATION_COLLECTION)
                .document(organizationId)
                .collection(PROJECTS_COLLECTION)
                .document(projectId)
                .collection(SCHOOLS_COLLECTION)
                .document(schoolId)
                .collection(ATTENDANCE_COLLECTION)
                .document(attendanceRecord.date)
                .set(attendanceRecord)
                .await()

            Results.success(Unit)
        } catch (e: Exception) {
            Results.error(e.message ?: "Unknown error")
        }
    }
}
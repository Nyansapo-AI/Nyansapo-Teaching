package com.nyansapoai.teaching.data.firebase.attendance

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.nyansapoai.teaching.data.remote.attendance.AttendanceRepository
import com.nyansapoai.teaching.domain.models.attendance.AttendanceRecord
import com.nyansapoai.teaching.utils.Results
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

    override suspend fun getAttendanceData(
        date: String,
        organizationId: String,
        projectId: String,
        schoolId: String
    ): Results<AttendanceRecord?> {

        if (date.isEmpty() ||
            organizationId.isEmpty() ||
            projectId.isEmpty() ||
            schoolId.isEmpty()
        ) {
            return Results.error("Invalid date or organization id or project id or school id")
        }

        Log.d("AttendanceRepositoryImp", "getAttendanceData: $date, $organizationId, $projectId, $schoolId")

        return try {
            val documentRef = firebaseDb.collection(ORGANIZATION_COLLECTION)
                .document(organizationId)
                .collection(PROJECTS_COLLECTION)
                .document(projectId)
                .collection(SCHOOLS_COLLECTION)
                .document(schoolId)
                .collection(ATTENDANCE_COLLECTION)
                .document(date)

            val snapshot = documentRef.get().await()
            Log.d("AttendanceRepositoryImp", "getAttendanceData: $snapshot")
            val attendanceRecord: AttendanceRecord? = if (snapshot.exists()) snapshot.toObject() else null
            Log.d("AttendanceRepositoryImp", "getAttendanceData: $attendanceRecord")
            Results.success(attendanceRecord)
        } catch (e: Exception) {
            Results.error(msg = e.message ?: "Unknown error")
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
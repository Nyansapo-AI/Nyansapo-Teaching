package com.nyansapoai.teaching.data.firebase.attendance

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
    ): Results<AttendanceRecord> {
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
            if (snapshot.exists()) {
                val attendanceRecord = snapshot.toObject<AttendanceRecord>()

                attendanceRecord?.let {
                    Results.success(attendanceRecord)
                } ?: Results.success(AttendanceRecord(date = date))

            } else {
                Results.success(AttendanceRecord(date = date))
            }
        } catch (e: Exception) {
            Results.error(msg = e.message ?: "Unknown error")
        }
    }

    override suspend fun submitAttendanceData(attendanceRecord: AttendanceRecord): Results<Unit> {
        return try {
            val documentRef = firebaseDb.collection(ORGANIZATION_COLLECTION)
                .document(/* organizationId */) // You need to provide organizationId
                .collection(PROJECTS_COLLECTION)
                .document(/* projectId */) // You need to provide projectId
                .collection(SCHOOLS_COLLECTION)
                .document(/* schoolId */) // You need to provide schoolId
                .collection(ATTENDANCE_COLLECTION)
                .document(attendanceRecord.date)

            documentRef.set(attendanceRecord).await()
            Results.success(Unit)
        } catch (e: Exception) {
            Results.error(e.message ?: "Unknown error")
        }
    }
}
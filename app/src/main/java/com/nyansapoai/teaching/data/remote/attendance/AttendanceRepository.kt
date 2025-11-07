package com.nyansapoai.teaching.data.remote.attendance

import com.nyansapoai.teaching.domain.models.attendance.AttendanceRecord
import com.nyansapoai.teaching.utils.Results

interface AttendanceRepository {
    suspend fun getAttendanceData(
        date: String,
        organizationId: String,
        projectId: String,
        schoolId: String,
    ): Results<AttendanceRecord?>

    suspend fun submitAttendanceData(attendanceRecord: AttendanceRecord, organizationId: String,projectId: String, schoolId: String ): Results<Unit>
}
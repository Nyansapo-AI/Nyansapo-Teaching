package com.nyansapoai.teaching.data.remote.attendance

import com.nyansapoai.teaching.domain.models.attendance.AttendanceRecord
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.flow.Flow

interface AttendanceRepository {
    suspend fun getAttendanceDataByDate(
        date: String,
        organizationId: String,
        projectId: String,
        schoolId: String,
    ): Flow<Results<AttendanceRecord?>>

    suspend fun submitAttendanceData(attendanceRecord: AttendanceRecord, organizationId: String,projectId: String, schoolId: String ): Results<Unit>
}
package com.nyansapoai.teaching.data.remote.students

import com.nyansapoai.teaching.domain.models.students.NyansapoStudent
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.flow.Flow

interface StudentsRepository {
    suspend fun getSchoolStudents(organizationId: String, projectId: String, schoolId: String, studentClass: Int?): Flow<Results<List<NyansapoStudent>>>
}
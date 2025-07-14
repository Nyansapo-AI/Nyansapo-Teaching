package com.nyansapoai.teaching.data.remote.school

import com.nyansapoai.teaching.domain.models.school.DetailedNyansapoSchool
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.flow.Flow

interface SchoolRepository {
    suspend fun getSchoolInfo(organizationId: String, projectId: String, schoolId: String): Flow<Results<DetailedNyansapoSchool>>
}
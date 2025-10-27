package com.nyansapoai.teaching.data.remote.survey

import com.nyansapoai.teaching.domain.models.school.LocalSchoolInfo
import com.nyansapoai.teaching.domain.models.survey.CreateHouseHoldInfo
import com.nyansapoai.teaching.domain.models.survey.HouseHoldInfo
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.flow.Flow

interface SurveyRepository {
    fun getHouseholdSurveys(
        organizationId: String,
        projectId: String,
        schoolId: String
    ): Flow<List<HouseHoldInfo>>

    suspend fun submitHouseholdSurvey(
        createHouseHold: CreateHouseHoldInfo,
        localSchoolInfo: LocalSchoolInfo?
    ): Results<Unit>

    fun getHouseholdSurveyById(
        organizationId: String,
        projectId: String,
        schoolId: String,
        id: String
    ): Flow<HouseHoldInfo?>
}
package com.nyansapoai.teaching.data.remote.survey

import com.nyansapoai.teaching.domain.models.survey.CreateHouseHoldInfo
import com.nyansapoai.teaching.domain.models.survey.HouseHoldInfo
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.flow.Flow

interface SurveyRepository {
    fun getHouseholdSurveys(
        village: String = "testVillage"
    ): Flow<List<HouseHoldInfo>>

    suspend fun submitHouseholdSurvey(
        createHouseHold: CreateHouseHoldInfo
    ): Results<Unit>

    fun getHouseholdSurveyById(
        id: String
    ): Flow<HouseHoldInfo?>
}
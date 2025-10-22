package com.nyansapoai.teaching.data.remote.survey

import com.nyansapoai.teaching.domain.models.survey.HouseHoldInfo
import kotlinx.coroutines.flow.Flow

interface SurveyRepository {
    fun getHouseholdSurveys(
        village: String
    ): Flow<List<HouseHoldInfo>>

    fun submitHouseholdSurvey(
        surveyData: HouseHoldInfo
    ): Boolean
}
package com.nyansapoai.teaching.presentation.survey.detailedHouseHold

import com.nyansapoai.teaching.domain.models.survey.HouseHoldInfo

data class DetailedHouseholdInfoState(
    val householdInfo: HouseHoldInfo? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
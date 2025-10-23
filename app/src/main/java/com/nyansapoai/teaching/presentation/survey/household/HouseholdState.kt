package com.nyansapoai.teaching.presentation.survey.household

import com.nyansapoai.teaching.domain.models.survey.HouseHoldInfo

data class HouseholdState(
    val households : List<HouseHoldInfo> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
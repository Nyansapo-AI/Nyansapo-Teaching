package com.nyansapoai.teaching.domain.models.assessments

data class AssessmentLevelDistribution(
    val type: String,
    val data: List<LevelDistributionData>
)

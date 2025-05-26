package com.nyansapoai.teaching.domain.dto.assessments

data class CreateAssessmentDTO(
    val name: String,
    val type: String,
    val startLevel: String,
    val assessmentNumber: Int,

)

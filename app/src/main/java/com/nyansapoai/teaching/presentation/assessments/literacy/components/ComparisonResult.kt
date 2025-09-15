package com.nyansapoai.teaching.presentation.assessments.literacy.components

data class ComparisonResult(
    val isMatch: Boolean,
    val similarityScore: Double,
    val normalizedExpected: String,
    val normalizedActual: String
)
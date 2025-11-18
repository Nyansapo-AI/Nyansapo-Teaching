package com.nyansapoai.teaching.domain.models.school

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class DetailedNyansapoSchool(
    val name: String = "",
    val createdAt: String = "",
    val lastUpdated: String = "",
    val total_camps: Int? = null,
    val total_students: Int? = null,
    val total_teachers: Int? = null
)

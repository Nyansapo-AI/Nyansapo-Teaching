package com.nyansapoai.teaching.domain.models.school

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class NyansapoSchool(
    val id: String = "",
    val name: String = "",
)

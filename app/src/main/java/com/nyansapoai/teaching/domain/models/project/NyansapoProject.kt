package com.nyansapoai.teaching.domain.models.project

import com.google.firebase.firestore.IgnoreExtraProperties
import com.nyansapoai.teaching.domain.models.school.NyansapoSchool

@IgnoreExtraProperties
data class NyansapoProject(
    val id: String = "",
    val is_manager: Boolean = false,
    val name: String = "",
    val schools: List<NyansapoSchool> = emptyList()
)

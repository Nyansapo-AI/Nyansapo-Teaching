package com.nyansapoai.teaching.domain.models.students

import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@IgnoreExtraProperties
data class NyansapoStudent(
    val id: String = "",
    val baseline: String = "",
    val grade: Int? = null,
    val createdAt: String = "",
    val group: String = "",
    val lastUpdated: String = "",
    val name: String = "",
    val sex: String = "",
    val first_name: String = "",
    val last_name: String = "",
)

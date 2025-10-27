package com.nyansapoai.teaching.domain.models.students

import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@IgnoreExtraProperties
data class NyansapoStudent(
    val id: String = "",
    val baseline: String? = null,
    val grade: Int? = null,
    val isLinked: Boolean = false,
//    val createdAt: String = "",
    val group: String = "",
//    val lastUpdated: String = "",
    val name: String = "",
    val sex: String = "",
    val has_done: Boolean = false,
    val first_name: String = "",
    val last_name: String = "",
)

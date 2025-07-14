package com.nyansapoai.teaching.domain.models.students

import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@IgnoreExtraProperties
data class NyansapoStudent(
    val baseline: String = "",
    @SerialName("class")
    val studentClass: Int? = null,
    val createdAt: String = "",
    val group: String = "",
    val lastUpdated: String = "",
    val name: String = "",
    val sex: String = "",
)

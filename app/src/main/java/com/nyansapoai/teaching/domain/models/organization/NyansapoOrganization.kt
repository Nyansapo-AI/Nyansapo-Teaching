package com.nyansapoai.teaching.domain.models.organization

import com.google.firebase.firestore.IgnoreExtraProperties
import com.nyansapoai.teaching.domain.models.project.NyansapoProject

@IgnoreExtraProperties
data class NyansapoOrganization(
    val id: String = "",
    val name: String = "",
    val projects: List<NyansapoProject> = emptyList()
)

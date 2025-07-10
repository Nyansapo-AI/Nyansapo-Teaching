package com.nyansapoai.teaching.domain.models.user

import com.google.firebase.firestore.IgnoreExtraProperties
import com.nyansapoai.teaching.domain.models.organization.NyansapoOrganization

@IgnoreExtraProperties
data class NyansapoUser(
    val createdAt: String = "",
    val email: String? = null,
    val name: String = "",
    val phone: String = "",
    val uid: String = "",
    val organizations: List<NyansapoOrganization> = emptyList()
)

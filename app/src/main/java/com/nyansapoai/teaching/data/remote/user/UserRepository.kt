package com.nyansapoai.teaching.data.remote.user

import com.nyansapoai.teaching.domain.models.user.NyansapoUser
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserDetails(): Flow<Results<NyansapoUser>>
}
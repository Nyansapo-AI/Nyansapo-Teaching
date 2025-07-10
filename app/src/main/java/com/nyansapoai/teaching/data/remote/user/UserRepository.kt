package com.nyansapoai.teaching.data.remote.user

import com.nyansapoai.teaching.domain.models.user.NyansapoUser
import com.nyansapoai.teaching.utils.Results

interface UserRepository {
    suspend fun getUserDetails(): Results<NyansapoUser>
}
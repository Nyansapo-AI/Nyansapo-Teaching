package com.nyansapoai.teaching.data.remote.school

interface SchoolRepository {
    suspend fun getSchoolInfo()
}
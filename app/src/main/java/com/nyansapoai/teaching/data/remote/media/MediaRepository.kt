package com.nyansapoai.teaching.data.remote.media

import com.nyansapoai.teaching.utils.Results

interface MediaRepository {
    suspend fun saveImage(imageByteArray: ByteArray): Results<String>

    suspend fun saveAudio(audioByteArray: ByteArray): Results<String>
}
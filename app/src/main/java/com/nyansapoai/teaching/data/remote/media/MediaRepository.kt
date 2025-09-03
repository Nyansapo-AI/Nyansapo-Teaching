package com.nyansapoai.teaching.data.remote.media

import com.nyansapoai.teaching.utils.Results

interface MediaRepository {
    suspend fun saveImage(imageByteArray: ByteArray, folder: String = "Nyansapo_Teaching_Numeracy_Assessment_Images"): Results<String>

    suspend fun saveAudio(audioByteArray: ByteArray, folder: String = "Nyansapo_Teaching_Literacy_Assessment_test_Audio", fileName: String = ""): Results<String>
}
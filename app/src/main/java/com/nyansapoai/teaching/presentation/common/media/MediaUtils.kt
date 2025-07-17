package com.nyansapoai.teaching.presentation.common.media

import android.util.Log
import java.io.File

object MediaUtils {
    const val TAG = "Media Utils"

    fun readImageFileByteArray(path: String): ByteArray? {
        return try {
            File(path).readBytes().also {
                Log.d(TAG, "read byte successful: $path")
            }
        }catch (e: Exception){
            Log.e(TAG,"Error reading media from: $path : ${e.message}",e)
            null
        }
    }

    fun cleanUpMediaFile(path: String) {
        try {
            val file = File(path)
            if (file.exists()) {
                if (file.delete()) {
                    Log.d(TAG, "Media file deleted successfully: $path")
                } else {
                    Log.e(TAG, "Failed to delete media file: $path")
                }
            } else {
                Log.w(TAG, "Media file does not exist: $path")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting media file: $path : ${e.message}", e)
        }
    }

    fun checkFileSizeInMB(file: File?, maxSizeMB: Int): Boolean {
        file?.let {
            if (!it.exists()) return false
            val maxSizeBytes = maxSizeMB * 1024 * 1024 // Convert MB to bytes
            return it.length() <= maxSizeBytes
        }
        return false
    }

}
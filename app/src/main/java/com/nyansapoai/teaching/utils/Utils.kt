package com.nyansapoai.teaching.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import kotlinx.datetime.toLocalDateTime
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    private val phoneRegex = Regex("^\\+?[0-9]{10,15}\$")
    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    fun isValidPhoneNumber(phone: String): Boolean {
        return phone.matches(phoneRegex)
    }

    fun secondsToTimerString(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    /**
     * Formats an ISO date string to show day and month if in current year,
     * or day, month and year if not in current year
     *
     * @param isoDateString Date string in ISO format like "2025-05-27T15:10:46.053Z"
     * @return Formatted date string like "27 May" or "27 May, 2025"
     */
    fun formatDate(isoDateString: String): String {
        try {
            val instant = kotlinx.datetime.Instant.parse(isoDateString)
            val localDate = instant.toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date

            val currentYear = kotlinx.datetime.Clock.System.now()
                .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).year

            val month = when (localDate.monthNumber) {
                1 -> "Jan"
                2 -> "Feb"
                3 -> "Mar"
                4 -> "Apr"
                5 -> "May"
                6 -> "Jun"
                7 -> "Jul"
                8 -> "Aug"
                9 -> "Sep"
                10 -> "Oct"
                11 -> "Nov"
                12 -> "Dec"
                else -> "Unknown"
            }

            return if (localDate.year == currentYear) {
                "${localDate.dayOfMonth} $month"
            } else {
                "${localDate.dayOfMonth} $month, ${localDate.year}"
            }
        } catch (e: Exception) {
            return "Invalid date"
        }
    }


    // Method 1: Convert ImageBitmap to ByteArray (most common)
    fun imageBitmapToByteArray(
        imageBitmap: ImageBitmap,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        quality: Int = 100
    ): ByteArray {
        val androidBitmap = imageBitmap.asAndroidBitmap()
        val outputStream = ByteArrayOutputStream()
        androidBitmap.compress(format, quality, outputStream)
        return outputStream.toByteArray()
    }


    fun saveImageBitmapToFile(imageBitmap: ImageBitmap, file: File, format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG, quality: Int = 100) {
        val androidBitmap = imageBitmap.asAndroidBitmap()
        FileOutputStream(file).use { out ->
            androidBitmap.compress(format, quality, out)
        }
    }


    /**
     * Saves an ImageBitmap to a file in the app's storage directory
     *
     * @param context Application context
     * @param filename Optional filename (generates timestamp-based name if null)
     * @param format Image compression format
     * @param quality Compression quality (0-100)
     * @return The saved File or null if saving failed
     */
    fun ImageBitmap.saveToImageFile(
        context: Context,
        filename: String? = null,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        quality: Int = 90
    ): File? {
        // Convert ImageBitmap to ByteArray
        val byteArray = this.toByteArray(format, quality)

        // Create filename with timestamp if not provided
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val extension = when (format) {
            Bitmap.CompressFormat.JPEG -> ".jpg"
            Bitmap.CompressFormat.PNG -> ".png"
            Bitmap.CompressFormat.WEBP -> ".webp"
            else -> ".png"
        }
        val actualFilename = filename ?: "IMG_$timeStamp$extension"

        // Get the directory for the app's private pictures directory
        val storageDir = context.getExternalFilesDir(null) ?: context.filesDir
        val imageFile = File(storageDir, actualFilename)

        return try {
            FileOutputStream(imageFile).use { outputStream ->
                outputStream.write(byteArray)
            }
            imageFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Converts an ImageBitmap to a ByteArray
     */
    fun ImageBitmap.toByteArray(
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        quality: Int = 90
    ): ByteArray {
        val bitmap = this.asAndroidBitmap()
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(format, quality, outputStream)
        println("ImageBitmap converted to ByteArray with format: ${outputStream.toByteArray()}")
        return outputStream.toByteArray()
    }


    /**
        * Clears the app's private pictures directory by deleting all files
     */
    fun clearAppPrivatePicturesDir(context: Context): Boolean {
        val picturesDir = context.getExternalFilesDir(null) ?: context.filesDir
        return try {
            picturesDir.listFiles()?.forEach { file ->
                if (file.isFile) file.delete()
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }





}
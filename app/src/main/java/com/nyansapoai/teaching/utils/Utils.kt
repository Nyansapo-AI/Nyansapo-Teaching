package com.nyansapoai.teaching.utils

import kotlinx.datetime.toLocalDateTime

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

}
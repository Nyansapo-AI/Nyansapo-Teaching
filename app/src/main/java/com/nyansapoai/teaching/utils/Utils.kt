package com.nyansapoai.teaching.utils

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

}
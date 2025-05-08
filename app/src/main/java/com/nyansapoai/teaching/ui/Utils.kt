package com.nyansapoai.teaching.ui

object Utils {

    private val phoneRegex = Regex("^\\+?[0-9]{10,15}\$")
    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    fun isValidPhoneNumber(phone: String): Boolean {
        return phone.matches(phoneRegex)
    }

}
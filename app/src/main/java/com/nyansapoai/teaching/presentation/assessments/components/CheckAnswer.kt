package com.nyansapoai.teaching.presentation.assessments.components

fun <T> checkAnswer(
    answer: T,
    correctAnswer: T
): Boolean {
    return when (answer) {
        is String -> answer.trim().equals(correctAnswer.toString().trim(), ignoreCase = true)
        is Int -> answer == correctAnswer
        is Double -> answer == correctAnswer
        else -> false
    }
}
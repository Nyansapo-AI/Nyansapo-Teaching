package com.nyansapoai.teaching.presentation.assessments.literacy.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

/**
 * Generates text highlighting the differences between expected and actual strings.
 * Matching words are highlighted in green, mismatches in red.
 *
 * @param expected The expected (correct) string
 * @param actual The actual response string from the student
 * @param ignoreCase Whether to ignore case differences in comparison
 * @return AnnotatedString with colored highlights
 */
fun generateComparisonText(
    expected: String,
    actual: String,
    ignoreCase: Boolean = true
): AnnotatedString {
    // Normalize strings based on parameters
    val normalizedExpected = if (ignoreCase) expected.lowercase() else expected
    val normalizedActual = if (ignoreCase) actual.lowercase() else actual

    // Split into words
    val expectedWords = normalizedExpected.split(Regex("\\s+"))
    val actualWords = normalizedActual.split(Regex("\\s+"))

    // Calculate word-level differences using longest common subsequence
    val matchedPositions = findMatchedWordPositions(expectedWords, actualWords)

    // Build annotated string
    return buildAnnotatedString {
        expectedWords.forEachIndexed { index, word ->
            val isMatched = matchedPositions.any { it.first == index }
            val style = if (isMatched) {
                SpanStyle(
                    color = Color(0xFF008000),
                    fontWeight = FontWeight.Bold
                )
            } else {
                SpanStyle(
                    color = Color(0xFFFF0000),
                    fontWeight = FontWeight.Bold,
//                    textDecoration = TextDecoration.Underline
                )
            }

            pushStyle(style)
            append(if (ignoreCase) word else expected.split(Regex("\\s+"))[index])
            pop()

            if (index < expectedWords.size - 1) {
                append(" ")
            }
        }
    }
}


/**
 * Finds matched word positions between two lists of words
 */
fun findMatchedWordPositions(
    expectedWords: List<String>,
    actualWords: List<String>
): List<Pair<Int, Int>> {
    val matches = mutableListOf<Pair<Int, Int>>()

    // Create a boolean array to track which words have been matched
    val expectedMatched = BooleanArray(expectedWords.size) { false }
    val actualMatched = BooleanArray(actualWords.size) { false }

    // First pass: find exact matches
    for (i in expectedWords.indices) {
        for (j in actualWords.indices) {
            if (!expectedMatched[i] && !actualMatched[j] && expectedWords[i] == actualWords[j]) {
                matches.add(Pair(i, j))
                expectedMatched[i] = true
                actualMatched[j] = true
                break
            }
        }
    }

    return matches
}
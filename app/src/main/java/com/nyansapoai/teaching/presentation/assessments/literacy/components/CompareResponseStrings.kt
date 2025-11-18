package com.nyansapoai.teaching.presentation.assessments.literacy.components

/**
 * Compares two strings for semantic similarity in the context of literacy assessment.
 *
 * @param expected The expected (correct) string
 * @param actual The actual response string from the student
 * @param ignoreCase Whether to ignore differences in capitalization (default: true)
 * @param ignoreWhitespace Whether to ignore extra spaces (default: true)
 * @param ignorePunctuation Whether to ignore punctuation differences (default: true)
 * @param similarity Required similarity threshold (0.0-1.0) (default: 0.85)
 * @return ComparisonResult containing match status and similarity score
 */
fun compareResponseStrings(
    expected: String,
    actual: String,
    ignoreCase: Boolean = true,
    ignoreWhitespace: Boolean = true,
    ignorePunctuation: Boolean = true,
    similarity: Double = 0.85
): ComparisonResult {
    var normalizedExpected = expected
    var normalizedActual = actual

    // Apply normalization based on parameters
    if (ignoreCase) {
        normalizedExpected = normalizedExpected.lowercase()
        normalizedActual = normalizedActual.lowercase()
    }

    if (ignoreWhitespace) {
        normalizedExpected = normalizedExpected.trim().replace("\\s+".toRegex(), " ")
        normalizedActual = normalizedActual.trim().replace("\\s+".toRegex(), " ")
    }

    if (ignorePunctuation) {
        val punctuationPattern = "[.,!?;:\"\']".toRegex()
        normalizedExpected = normalizedExpected.replace(punctuationPattern, "")
        normalizedActual = normalizedActual.replace(punctuationPattern, "")
    }

    // Calculate similarity score using Levenshtein distance
    val distance = levenshteinDistance(normalizedExpected, normalizedActual)
    val maxLength = maxOf(normalizedExpected.length, normalizedActual.length)
    val similarityScore = if (maxLength > 0) (maxLength - distance) / maxLength.toDouble() else 1.0

    // Exact match check after normalization
    val isExactMatch = normalizedExpected == normalizedActual

    // Similarity match based on threshold
    val isSimilarMatch = similarityScore >= similarity

    return ComparisonResult(
        isMatch = isExactMatch || isSimilarMatch,
        similarityScore = similarityScore,
        normalizedExpected = normalizedExpected,
        normalizedActual = normalizedActual
    )
}

// Helper function to calculate Levenshtein distance
private fun levenshteinDistance(s1: String, s2: String): Int {
    val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }

    for (i in 0..s1.length) {
        dp[i][0] = i
    }

    for (j in 0..s2.length) {
        dp[0][j] = j
    }

    for (i in 1..s1.length) {
        for (j in 1..s2.length) {
            dp[i][j] = minOf(
                dp[i-1][j] + 1,
                dp[i][j-1] + 1,
                dp[i-1][j-1] + if (s1[i-1] == s2[j-1]) 0 else 1
            )
        }
    }

    return dp[s1.length][s2.length]
}

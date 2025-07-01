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


/**
 * Generates HTML highlighting the differences between expected and actual strings.
 * Matching words are highlighted in green, mismatches in red.
 *
 * @param expected The expected (correct) string
 * @param actual The actual response string from the student
 * @param ignoreCase Whether to ignore case differences in comparison
 * @return HTML string with colored highlights
 */
fun generateComparisonHtml(
    expected: String,
    actual: String,
    ignoreCase: Boolean = true
): String {
    // Normalize strings based on parameters
    val normalizedExpected = if (ignoreCase) expected.lowercase() else expected
    val normalizedActual = if (ignoreCase) actual.lowercase() else actual

    // Split into words
    val expectedWords = normalizedExpected.split(Regex("\\s+"))
    val actualWords = normalizedActual.split(Regex("\\s+"))

    // Calculate word-level differences using longest common subsequence
    val matchedPositions = findMatchedWordPositions(expectedWords, actualWords)

    // Build HTML output
    val htmlBuilder = StringBuilder()
    htmlBuilder.append("<div class='comparison-result'>")
    htmlBuilder.append("<div class='expected-text'>")

    expectedWords.forEachIndexed { index, word ->
        val isMatched = matchedPositions.any { it.first == index }
        val colorClass = if (isMatched) "correct-word" else "missed-word"
        htmlBuilder.append("<span class='$colorClass'>")
        htmlBuilder.append(escapeHtml(word))
        htmlBuilder.append("</span> ")
    }

    htmlBuilder.append("</div>")
    htmlBuilder.append("</div>")

    // Return HTML with embedded CSS
    return """
        <style>
        .comparison-result {
            font-family: Arial, sans-serif;
            line-height: 1.5;
        }
        .correct-word {
            color: #008000; /* green */
            font-weight: bold;
        }
        .missed-word {
            color: #FF0000; /* red */
            font-weight: bold;
            text-decoration: underline;
        }
        </style>
        ${htmlBuilder}
    """.trimIndent()
}

/**
 * Finds matched word positions between two lists of words
 */
private fun findMatchedWordPositions(
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

/**
 * Escapes HTML special characters
 */
private fun escapeHtml(text: String): String {
    return text.replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
}
package com.nyansapoai.teaching.presentation.assessments.numeracy.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyAssessmentContent
import com.nyansapoai.teaching.presentation.assessments.components.InputResponseAlert
import com.nyansapoai.teaching.presentation.common.components.AppCircularLoading
import com.nyansapoai.teaching.presentation.common.components.AppSimulateNavigation



enum class NumeracyAssessmentLevel(
    val label: String
) {
    COUNT_MATCH("Count and Match"),
    ADDITION("Addition"),
    SUBTRACTION("Subtraction"),
    MULTIPLICATION("Multiplication"),
    DIVISION("Division"),
    NUMBER_RECOGNITION("Number Recognition"),
    WORD_PROBLEM("Word Problem"),
}

package com.nyansapoai.teaching.presentation.survey.takeSurvey.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun ChildLearningEnvironmentContent(
    modifier: Modifier = Modifier,
    isQuietPlaceAvailable: Boolean?,
    onQuietPlaceChanged: (Boolean) -> Unit,
    hasLearningMaterials: Boolean?,
    onHasLearningMaterialsChanged: (Boolean) -> Unit
) {


    Column(
        modifier = modifier
            .imePadding()
    ) {
        YesNoOption(
            text = "Does the child have a quiet place to study?",
            isYes = isQuietPlaceAvailable,
            onChange = { onQuietPlaceChanged(it) }
        )


        YesNoOption(
            text = "Does the household have books or learning materials?",
            isYes = hasLearningMaterials,
            onChange = { onHasLearningMaterialsChanged(it) }
        )

    }
}
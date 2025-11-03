package com.nyansapoai.teaching.presentation.survey.takeSurvey.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.nyansapoai.teaching.presentation.common.components.AppCheckBox
import com.nyansapoai.teaching.presentation.common.components.AppDropDownItem
import com.nyansapoai.teaching.presentation.common.components.AppDropDownMenu
import com.nyansapoai.teaching.presentation.common.components.AppTextField

@Composable
fun ChildLearningEnvironmentContent(
    modifier: Modifier = Modifier,
    isQuietPlaceAvailable: Boolean?,
    onQuietPlaceChanged: (Boolean) -> Unit,
    hasLearningMaterials: Boolean?,
    onHasLearningMaterialsChanged: (Boolean) -> Unit,
    hasMissedSchool: Boolean?,
    onHasMissedSchoolChanged: (Boolean) -> Unit,
    showMissedReasonDropdown: Boolean,
    onShowMissedReasonDropdownChanged: (Boolean) -> Unit,
    missedReason: String,
    onMissedReasonChanged: (String) -> Unit,
    otherMissedReason: String,
    onOtherMissedReasonChanged: (String) -> Unit
) {
    val missedReasons = remember {
        listOf("Illness", "Lack of fees", "Lack of interest", "Other")
    }

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


        YesNoOption(
            text = "Has the child missed school in the last month?",
            isYes = hasMissedSchool,
            onChange = { onHasMissedSchoolChanged(it) }
        )

        AnimatedVisibility(visible = hasMissedSchool == true) {
            Column {
                AppDropDownMenu(
                    expanded = showMissedReasonDropdown,
                    label = "What was the main reason for missing school?",
                    placeholder = "Select reason",
                    onClick = { onShowMissedReasonDropdownChanged(!showMissedReasonDropdown) },
                    value = missedReason
                ) {
                    missedReasons.forEach { reason ->
                        AppDropDownItem(
                            item = reason,
                            isSelected = missedReason == reason,
                            onClick = { onMissedReasonChanged(reason) }
                        )
                    }
                }

                AnimatedVisibility(visible = missedReason == "Other") {
                    AppTextField(
                        label = "Specify Other Reason",
                        value = otherMissedReason,
                        onValueChanged = onOtherMissedReasonChanged,
                        placeholder = "Describe the reason",
                    )
                }
            }
        }
    }
}
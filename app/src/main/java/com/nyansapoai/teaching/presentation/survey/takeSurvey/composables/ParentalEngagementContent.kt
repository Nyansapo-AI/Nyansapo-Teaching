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
fun ParentalEngagementContent(
    modifier: Modifier = Modifier,
    isSchoolAgePresent: Boolean,
    onSchoolAgeChanged: (Boolean) -> Unit,
    showWhoHelpsDropdown: Boolean,
    onShowWhoHelpsDropdownChanged: (Boolean) -> Unit,
    whoHelps: String,
    onWhoHelpsChanged: (String) -> Unit,
    otherWhoHelps: String,
    onOtherWhoHelpsChanged: (String) -> Unit,
    showDiscussDropdown: Boolean,
    onShowDiscussDropdownChanged: (Boolean) -> Unit,
    discussFrequency: String,
    onDiscussFrequencyChanged: (String) -> Unit,
    attendMeetings: Boolean,
    onAttendMeetingsChanged: (Boolean) -> Unit,
    monitorAttendance: Boolean,
    onMonitorAttendanceChanged: (Boolean) -> Unit
) {
    val whoHelpsOptions = remember { listOf("Mother", "Father", "Both", "Other") }
    val discussOptions = remember { listOf("Always", "Sometimes", "Rarely", "Never") }

    Column(
        modifier = modifier
            .imePadding()
    ) {
        AppCheckBox(
            text = "Is there any school-age child (6–17 years) in this household?",
            checked = isSchoolAgePresent,
            onCheckedChange = {onSchoolAgeChanged(it)}

        )

        // If No -> skip rest (AnimatedVisibility hides them)
        AnimatedVisibility(isSchoolAgePresent) {
            Column {
                // D02
                AppDropDownMenu(
                    expanded = showWhoHelpsDropdown,
                    label = "Who helps the child with homework?",
                    placeholder = "Select who helps",
                    onClick = { onShowWhoHelpsDropdownChanged(!showWhoHelpsDropdown) },
                    value = whoHelps
                ) {
                    whoHelpsOptions.forEach { option ->
                        AppDropDownItem(
                            item = option,
                            isSelected = whoHelps == option,
                            onClick = { onWhoHelpsChanged(option) }
                        )
                    }
                }

                // show specify field when Other selected
                AnimatedVisibility(visible = whoHelps == "Other") {
                    AppTextField(
                        label = "Specify Other",
                        value = otherWhoHelps,
                        onValueChanged = onOtherWhoHelpsChanged,
                        placeholder = "Specify who helps",
                    )
                }

                // D03
                AppDropDownMenu(
                    expanded = showDiscussDropdown,
                    label = "How often do you discuss your child’s learning with teachers?",
                    placeholder = "Select frequency",
                    onClick = { onShowDiscussDropdownChanged(!showDiscussDropdown) },
                    value = discussFrequency
                ) {
                    discussOptions.forEach { option ->
                        AppDropDownItem(
                            item = option,
                            isSelected = discussFrequency == option,
                            onClick = { onDiscussFrequencyChanged(option) }
                        )
                    }
                }

                // D04
                AppCheckBox(
                    text = "Do you attend school meetings or parent–teacher forums?",
                    checked = attendMeetings,
                    onCheckedChange = onAttendMeetingsChanged
                )

                // D05
                AppCheckBox(
                    text = "Do you monitor your child’s school attendance?",
                    checked = monitorAttendance,
                    onCheckedChange = onMonitorAttendanceChanged
                )
            }
        }
    }
}
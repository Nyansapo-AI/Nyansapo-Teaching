package com.nyansapoai.teaching.presentation.survey.takeSurvey.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nyansapoai.teaching.presentation.common.components.AppTextField

@Composable
fun IdentificationAndContentContent(
    modifier: Modifier = Modifier,
    name: String,
    nameError: String?,
    consentGiven: Boolean?,
    onNameChanged: (String) -> Unit,
    onConsentChanged: (Boolean) -> Unit
)
{
    Column(
        modifier = modifier
    ) {
        AppTextField(
            required = true,
            label = "Interviewee",
            value = name,
            error = nameError,
            onValueChanged = onNameChanged,
            placeholder = "Enter your full name",
        )

        YesNoOption(
            text = " Has the respondent given consent to participate?",
            isYes = consentGiven,
            onChange = onConsentChanged
        )
    }
}
package com.nyansapoai.teaching.presentation.survey.takeSurvey.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nyansapoai.teaching.presentation.common.components.AppTextField

@Composable
fun IdentificationAndContentContent(
    modifier: Modifier = Modifier,
    interviewee: String,
    interviewer: String,
    interviewerError: String?,
    nameError: String?,
    consentGiven: Boolean?,
    onInterviewerChanged: (String) -> Unit,
    onIntervieweeChanged: (String) -> Unit,
    onConsentChanged: (Boolean) -> Unit
)
{
    Column(
        modifier = modifier
    )
    {

        AppTextField(
            required = true,
            showError = interviewerError != null || interviewer.isEmpty(),
            label = "Interviewer(Volunteer)",
            value = interviewer,
            error = interviewerError,
            onValueChanged = onInterviewerChanged,
            placeholder = "Enter your full name",
        )

        AppTextField(
            required = true,
            showError = nameError != null || interviewee.isEmpty(),
            label = "Interviewee",
            value = interviewee,
            error = nameError,
            onValueChanged = onIntervieweeChanged,
            placeholder = "Enter your full name",
        )

        YesNoOption(
            text = " Has the respondent given consent to participate?",
            showError = consentGiven == null,
            isYes = consentGiven,
            onChange = onConsentChanged
        )
    }
}
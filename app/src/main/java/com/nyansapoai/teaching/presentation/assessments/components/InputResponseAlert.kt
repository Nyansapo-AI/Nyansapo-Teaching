package com.nyansapoai.teaching.presentation.assessments.components

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputResponseAlert(
    modifier: Modifier = Modifier,
    response: Int? = null,
    responseError: String? = null,
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    BasicAlertDialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = true
        )
    ) {
        AlertDialog(
            onDismissRequest = {},
            containerColor = MaterialTheme.colorScheme.tertiary,
            title = {

                response?.let {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Your Answer",
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                        Text(
                            text = "$response",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 120.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }


                responseError?.let {
                    Text(
                        text = "We can't read your answer, Please try again.",
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )
                }

            },
            text = {
                Text(
                    text = response?.let { "Press confirm to submit your answer or dismiss to cancel."  } ?: "",
                    color = MaterialTheme.colorScheme.onTertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (responseError == null) {
                            onDismiss()
                        } else {
                            onConfirm()
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(text = responseError?.let { "Try Again" } ?: "Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                    )

                ) {
                    Text(
                        text = response?.let { "Dismiss" } ?: ""
                    )
                }
            },
            modifier = modifier
        )
    }
}



@Preview
@Composable
private fun InputResponseAlertPreview() {
    InputResponseAlert()
}
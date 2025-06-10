package com.nyansapoai.teaching.presentation.assessments.components

import androidx.compose.animation.AnimatedVisibility
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
    showAlert: Boolean ,
    response: Int?,
    responseError: String?,
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {


    AnimatedVisibility(
        visible = showAlert
    ){
        BasicAlertDialog(
            onDismissRequest = {},
        ) {
            AlertDialog(
                onDismissRequest = {},
                  title = {

                    if (responseError != null){
                        Text(
//                            text = "We can't read your answer, Please try again.",
                            text = responseError,
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Center,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    response?.let {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Your Answer",
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "$response",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 120.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }





                },
                text = {
                    Text(
                        text = response?.let { "Press confirm to submit your answer or dismiss to cancel."  } ?: "",
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (responseError != null) {
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
                            contentColor = MaterialTheme.colorScheme.onBackground,
                        )

                    ) {
                        Text(
                            text = "Dismiss"
                        )
                    }
                },
                modifier = modifier
            )
        }

    }
}



@Preview
@Composable
private fun InputResponseAlertPreview() {
//    InputResponseAlert()
}
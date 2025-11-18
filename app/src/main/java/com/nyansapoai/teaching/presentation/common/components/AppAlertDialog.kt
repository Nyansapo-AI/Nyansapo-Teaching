package com.nyansapoai.teaching.presentation.common.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.ui.theme.lightPrimary

@Composable
fun AppAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    confirmLabel: String = "Confirm",
    iconTint: Color = MaterialTheme.colorScheme.secondary,
    dialogText: String,
    @DrawableRes icon: Int = R.drawable.info,
) {
    AlertDialog(
        shape = RoundedCornerShape(8),
        containerColor = MaterialTheme.colorScheme.onPrimary,
        textContentColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.primary,
        iconContentColor = MaterialTheme.colorScheme.secondary,
        icon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = dialogTitle,
                tint = iconTint
            )
        },
        title = {
            Text(
                text = dialogTitle,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            )
        },
        text = {
            Text(text = dialogText, style = MaterialTheme.typography.bodyMedium)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                colors = ButtonColors(
                    containerColor = lightPrimary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
                onClick = {
                    onConfirmation()
                },
            ) {
                Text(confirmLabel)
            }
        },
        dismissButton = {
            TextButton(
                colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.background,
                        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    ),
                onClick = {
                    onDismissRequest()
                },
            ) {
                Text("Dismiss")
            }
        },
    )
}

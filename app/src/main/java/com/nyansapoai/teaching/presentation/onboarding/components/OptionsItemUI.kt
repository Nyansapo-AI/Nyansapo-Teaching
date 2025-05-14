package com.nyansapoai.teaching.presentation.onboarding.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OptionsItemUI(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        border = BorderStroke(
            width = 2.dp,
            color = if(isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
        ),
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = text,
            color = if(isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
        )
    }
}
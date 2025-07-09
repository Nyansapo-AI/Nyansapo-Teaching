package com.nyansapoai.teaching.presentation.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppButton(
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit,
    shape: Shape? = RoundedCornerShape(5.dp),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary
    ),
    height: Dp = 54.dp,
    content: @Composable () -> Unit,

    ) {
    Button(
        enabled = enabled,
        shape = shape ?: ButtonDefaults.shape,
        colors = colors,
        onClick = onClick,
        modifier = modifier
            .height(height),
    ) {
        when(isLoading){
            true -> {
                CircularProgressIndicator()
            }
            false -> {
                content()
            }
        }
    }
}

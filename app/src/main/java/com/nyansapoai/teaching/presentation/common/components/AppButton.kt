package com.nyansapoai.teaching.presentation.common.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppButton(
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit = {},
    shape: Shape? = RoundedCornerShape(5.dp),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary
    ),
    height: Dp = 54.dp,
    content: @Composable () -> Unit = { /* Default empty content */ },

    )
{

    var isPressed: Boolean by remember { mutableStateOf(false) }
    val animatedWidth: Dp by animateDpAsState(
        if (isPressed) 4.dp else 0.dp,
        label = "width",

    )


    Button(
        enabled = enabled,
        shape = shape ?: ButtonDefaults.shape,
        colors = colors,
        onClick = {
            if (!isLoading) {
                onClick()
            }
        },
        modifier = modifier
            .height(height)

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

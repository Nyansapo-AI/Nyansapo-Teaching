package com.nyansapoai.teaching.presentation.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Composable
fun AppErrorToastContent(
    modifier: Modifier = Modifier,
    error: String?= null,
    content: @Composable () -> Unit= {
        Text(
            text = error ?: "An error occurred. Please try again.",
            color = MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(12.dp),
        )

    }
) {
    AnimatedVisibility(
        visible = error != null,
        modifier = modifier
            .zIndex(1f)
            .padding(12.dp)
            .clip(RoundedCornerShape(10))
            .background(MaterialTheme.colorScheme.tertiary)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.error,
                shape = RoundedCornerShape(10)
            )
    ) {
        content.invoke()
    }
}
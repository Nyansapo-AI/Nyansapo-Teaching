package com.nyansapoai.teaching.presentation.common.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class StepContent(
    val screen: @Composable() (Modifier) -> Unit,
    val onSubmit: () -> Unit,
    val title: String,
)
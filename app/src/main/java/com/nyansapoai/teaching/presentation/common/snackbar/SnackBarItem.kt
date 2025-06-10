package com.nyansapoai.teaching.presentation.common.snackbar

import androidx.compose.material3.SnackbarDuration

data class SnackBarItem(
    val message: String,
    val isError: Boolean = false,
    val duration: SnackbarDuration = SnackbarDuration.Long,
)
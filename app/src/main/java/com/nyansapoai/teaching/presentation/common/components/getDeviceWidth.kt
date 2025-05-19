package com.nyansapoai.teaching.presentation.common.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun getDeviceWidth(): Int {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp
}
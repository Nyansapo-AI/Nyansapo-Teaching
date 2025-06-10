package com.nyansapoai.teaching.presentation.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.LoadingIndicatorDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp



@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppCircularLoading(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        LoadingIndicator(
            modifier = Modifier
                .size(60.dp),
            color = MaterialTheme.colorScheme.secondary,
            polygons =  LoadingIndicatorDefaults.IndeterminateIndicatorPolygons
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AppCircularLoadingPreview() {
    AppCircularLoading(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
}

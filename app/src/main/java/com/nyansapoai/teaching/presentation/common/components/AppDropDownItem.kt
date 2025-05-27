package com.nyansapoai.teaching.presentation.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppDropDownItem(
    modifier: Modifier = Modifier,
    item: String,
    isSelected: Boolean,
    onClick: () -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .clickable(
                onClick = onClick
            )
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)

    ) {

        Text(
            text = item,
            fontSize = 16.sp,
            style = MaterialTheme.typography.titleSmall
        )

        if (isSelected){
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "selected",
                tint = MaterialTheme.colorScheme.secondary
            )
        }

    }
}
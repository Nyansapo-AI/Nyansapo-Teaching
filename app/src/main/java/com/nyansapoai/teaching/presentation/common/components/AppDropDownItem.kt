package com.nyansapoai.teaching.presentation.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nyansapoai.teaching.R

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
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
            .padding(horizontal = 8.dp, vertical = 8.dp)

    ) {

        Text(
            text = item,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall
        )

        /*
        if (isSelected){
            Icon(
                painter = painterResource(R.drawable.check),
                contentDescription = "selected",
                tint = MaterialTheme.colorScheme.secondary
            )
        }*/

        /*
        AppCheckBox(
            checked = isSelected,
            text = "",
            onCheckedChange = {}
        )
         */

        RadioButton (
            selected = isSelected,
            onClick = {},
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.secondary,
                unselectedColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
            )
        )

    }
}
package com.nyansapoai.teaching.presentation.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.R

@Composable
fun AppDropDownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    label: String,
    placeholder: String,
    value: String,
    onClick: () -> Unit,
    error: String? = null,
    required: Boolean = false,
    content: @Composable () -> Unit
){
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        AppTextField(
            enabled = false,
            required = required,
            value = value,
            label = label,
            placeholder = placeholder,
            onValueChanged = {},
            keyboardType = KeyboardType.Text,
            error = error,
            trailingIcon = {
                IconButton(
                    onClick = onClick,
                    content = {
                        Icon(
                            painter = painterResource(if (expanded) R.drawable.arrow_up else R.drawable.arrow_down),
                            contentDescription = "available assets",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                )
            },
            textFieldColors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
                disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier
        )


        AnimatedVisibility(
            expanded,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ){
            Card(
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                ),
                colors = CardDefaults.cardColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.tertiary
                ),
                modifier = Modifier
                    .fillMaxWidth()
//                    .padding(vertical = 8.dp)
            ) {
                content()
            }
        }
    }
}
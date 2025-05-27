package com.nyansapoai.teaching.presentation.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    label: String = "",
    value: String,
    placeholder: String,
    error: String? = null,
    singleLine: Boolean = true,
    onValueChanged: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    passwordVisible: Boolean = true,
    enabled: Boolean = true,
    required: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    onImeActionDone: () -> Unit = {},
    textFieldColors: TextFieldColors = TextFieldDefaults.colors(
        cursorColor = MaterialTheme.colorScheme.secondary,
        focusedContainerColor = MaterialTheme.colorScheme.tertiary,
        unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        disabledTextColor = MaterialTheme.colorScheme.onBackground,
    ) ,
    trailingIcon: @Composable () -> Unit = {},
    leadingIcon: @Composable() (() -> Unit)? = null,
    textFieldModifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
    ) {
        if (label.isNotBlank()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(vertical = 4.dp),
                )

                if (required) {
                    Text(
                        text = " *",
                        style =
                            MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.error,
                            ),
                        modifier = Modifier.padding(vertical = 4.dp),
                    )
                }
            }
        }

        TextField(
            enabled = enabled,
            value = value,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.labelLarge.copy(color = Color.Gray),
                )
            },
            maxLines = 1,
            colors =textFieldColors,
            leadingIcon = leadingIcon,
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction, ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onImeActionDone()
                }
            ),
            onValueChange = onValueChanged,
            shape = RoundedCornerShape(5.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = textFieldModifier.fillMaxWidth().padding(vertical = 8.dp).testTag(label.lowercase()),
            trailingIcon = trailingIcon,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground)
        )
        if (error != null) {
            Text(
                text = error,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.error,
            )
        }else {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AppTextFieldPreview(){
    AppTextField(
        value = "",
        placeholder = "Name",
        error = null,
        onValueChanged = {}
    )
}

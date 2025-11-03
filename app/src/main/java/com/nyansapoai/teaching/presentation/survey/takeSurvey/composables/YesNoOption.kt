package com.nyansapoai.teaching.presentation.survey.takeSurvey.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.presentation.common.components.AppCheckBox

@Composable
fun YesNoOption(
    modifier: Modifier = Modifier,
    text: String,
    isYes: Boolean?,
    onChange: (Boolean) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
//                .padding(start = 12.dp)
        )

        Column {

            AppCheckBox(
                text = "Yes",
                onCheckedChange = { onChange(true) },
                checked = isYes == true
            )
            AppCheckBox(
                text = "No",
                onCheckedChange = {onChange(false)},
                checked = isYes == false
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun YesNoOptionPreview() {
    var isYes by remember { mutableStateOf<Boolean?>(null) }

    YesNoOption(
        text = "This is a test string",
        isYes = isYes,
        onChange = {option -> isYes = option}
    )
}
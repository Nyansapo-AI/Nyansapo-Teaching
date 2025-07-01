package com.nyansapoai.teaching.presentation.assessments.literacy.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.presentation.common.components.AppButton

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MultichoiceQuestionItemUI(
    modifier: Modifier = Modifier,
    question: String = "Select the correct answer from the choices below. Make sure to read each choice carefully before making your selection?",
    choices: List<String> = listOf(
        "short text",
        "default text",
        "medium length text, as it is used to test the layout of the checkbox and text",
        "slightly longer text that is used to test the layout of the checkbox and text, making sure it wraps correctly and does not overflow the bounds of the container",
    ),
    orientation: Orientation = Orientation.Vertical,
    selectedChoice: String? = null,
    onChoiceSelected: (String) -> Unit = { /* Handle choice selection */ },
    canSubmit: Boolean = false,
    onSubmit: () -> Unit = { /* Handle submit action */ }

) {


    when(orientation) {
        Orientation.Horizontal -> {
            // Handle horizontal orientation if needed

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
                modifier = modifier
                    .fillMaxWidth()
            ) {

                Text(
                    text = question,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = modifier
                        .weight(0.5f)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(40.dp),
                ) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.Start,
                            modifier = modifier
                        ) {
                            choices.forEach { choice ->
                                ChoiceItem(
                                    choice = choice,
                                    isSelected = selectedChoice == choice,
                                    onClick = { onChoiceSelected(choice) }
                                )
                            }
                        }
                    }

                    item {
                        AppButton(
                            enabled = true,
                            onClick = {},
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Submit",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }
                }
            }
        }
        Orientation.Vertical -> {
            // Default vertical orientation
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(40.dp),
                modifier = modifier
                    .fillMaxSize()
                    .widthIn(max = 600.dp)
            ) {


                Text(
                    text = question,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = modifier
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start,
                    modifier = modifier.fillMaxWidth()
                ) {
                    choices.forEach { choice ->
                        ChoiceItem(
                            choice = choice,
                            isSelected = selectedChoice == choice,
                            onClick = { onChoiceSelected(choice) }
                        )
                    }
                }

                AppButton(
                    enabled = canSubmit,
                    onClick = onSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Submit",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

            }

        }
    }

}



@Preview(showBackground = true,)
@Composable
fun ChoiceItem(
    modifier: Modifier = Modifier,
    choice: String = "long text that is used to test the layout of the checkbox and text, making sure it wraps correctly and does not overflow the bounds of the container",
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
){
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
        else MaterialTheme.colorScheme.tertiary
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.secondary
        else MaterialTheme.colorScheme.outline
    )


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10))
            .border(
                width = 4.dp,
                color = borderColor,
                shape = RoundedCornerShape(10)
            )
            .clickable(
                onClick = onClick,
                enabled = true
            )
            .background(backgroundColor)
            .padding(12.dp)
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.secondary,
                unselectedColor = MaterialTheme.colorScheme.outline
            )
        )

        Text(
            text = choice,
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier
        )
    }
}
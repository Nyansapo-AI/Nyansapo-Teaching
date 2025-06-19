package com.nyansapoai.teaching.presentation.assessments.numeracy.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.common.components.AppButton
import com.nyansapoai.teaching.presentation.common.components.AppTouchInput
import com.nyansapoai.teaching.presentation.common.components.CapturableComposable

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NumeracyWordProblem(
    modifier: Modifier = Modifier,
    title: String = "Word Problem",
    wordProblem: String = "A farmer has 12 apples. He gives 3 apples to his friend. How many apples does he have left?",
    onCaptureWorkAreaContent: (ByteArray) -> Unit = {},
    shouldCaptureWorkArea: Boolean = false,
    shouldCaptureAnswer: Boolean = false,
    onCaptureAnswerContent: (ByteArray) -> Unit = {},
    onSubmit: () -> Unit
    ) {

    var isEraserMode by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
     ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )


            ButtonGroup(
                overflowIndicator = { menuState ->
                    FilledIconButton(
                        onClick = {
                            if (menuState.isExpanded) {
                                menuState.dismiss()
                            } else {
                                menuState.show()
                            }
                        },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.more_vert),
                            contentDescription = "More options",
                        )
                    }
                },
                modifier =  Modifier,
                content = {
                    clickableItem(
                        onClick = {
                            isEraserMode = false
                        },
                        icon = {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (!isEraserMode) MaterialTheme.colorScheme.secondary.copy(
                                            alpha = 0.3f
                                        ) else Color.Transparent
                                    )
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.pencil),
                                    contentDescription = "use pencil to write",
                                    modifier = Modifier
                                        .size(20.dp)
                                )
                            }
                        },
                        label = "Use Pencil",
                    )
                    clickableItem(
                        onClick = {
                            isEraserMode = true
                        },
                        icon = {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isEraserMode) MaterialTheme.colorScheme.secondary.copy(
                                            alpha = 0.3f
                                        ) else Color.Transparent
                                    )
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.eraser),
                                    contentDescription = "use eraser to erase",
                                    modifier = Modifier
                                        .size(20.dp)
                                )
                            }
                        },
                        label = "Use eraser",
                    )
                }
            )

            Text(
                text = wordProblem,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )


            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)

            ) {
                Text(
                    text = "Answer",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                )

                Box(
                    modifier = Modifier
                        .widthIn(max = 240.dp, min = 100.dp)
                        .heightIn(min = 100.dp, max = 150.dp)
                        .background(MaterialTheme.colorScheme.tertiary)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                        )
                ) {
                    CapturableComposable(
                        onCapturedByteArray = onCaptureAnswerContent,
                        shouldCapture = shouldCaptureAnswer,
                        content = {
                            AppTouchInput(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.tertiary),
                                isEraserMode = isEraserMode,
                            )
                        }
                    )
                }

            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Work Area",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )

                Box(
                    modifier = Modifier
                        .widthIn(max = 420.dp, min = 100.dp)
                        .heightIn(min = 300.dp, max = 400.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                        )
                ) {

                    CapturableComposable(
                        onCapturedByteArray = onCaptureWorkAreaContent,
                        shouldCapture = shouldCaptureWorkArea,
                        content = {
                            AppTouchInput(
                                isEraserMode = isEraserMode,
                                brushColor = Color.Green
                            )
                        },
                    )
                }
            }

            AppButton(
                onClick = onSubmit,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    "Submit",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

        }

    }
}
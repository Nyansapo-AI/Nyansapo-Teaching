package com.nyansapoai.teaching.presentation.assessments.numeracy.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.common.components.AppButton
import com.nyansapoai.teaching.presentation.common.components.AppShowInstructions
import com.nyansapoai.teaching.presentation.common.components.AppTouchInput
import com.nyansapoai.teaching.presentation.common.components.ScreenshotComposable

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NumeracyHorizontalArithmeticOperationsUI(
    modifier: Modifier = Modifier,
    firstNumber: Int = 12,
    secondNumber: Int = 4,
    operationType: OperationType = OperationType.DIVISION,
    onAnswerFilePathChange: (String) -> Unit = {},
    onWorkAreaFilePathChange: (String) -> Unit = {},
    shouldCapture: Boolean = false,
    onSubmit: () -> Unit = {},
    isLoading: Boolean = false,
) {
    var isEraserMode by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    )
    {
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

        ScreenshotComposable(
            shouldCapture = shouldCapture,
            fileName = "work_area",
            onFilePathChange = { path ->
                onWorkAreaFilePathChange(path)
            },
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary)
                )
                {

                    AppTouchInput(
                        isEraserMode = isEraserMode,
                        brushColor = Color.Green,
                        modifier = Modifier
                            .fillMaxSize()
                    )

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth()
                    ) {

                        HorizontalOperationItem(
                            operationSymbol = operationType.symbol,
                            firstNumber = firstNumber,
                            secondNumber = secondNumber
                        )

                        AppShowInstructions(
                            instructionAudio = R.raw.write_your_answer_in_the_box,
                            instructionsTitle = "Answer Box",
                            instructionsDescription = "Write your answer in the box.",
                            content = {
                                Box(
                                    modifier = Modifier
                                        .widthIn(max = 270.dp, min = 100.dp)
                                        .heightIn(min = 100.dp, max = 150.dp)
                                ) {
                                    ScreenshotComposable(
                                        shouldCapture = shouldCapture,
                                        fileName = "answer_area",
                                        onFilePathChange = { path ->
                                            onAnswerFilePathChange(path)
                                        },
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
                        )
                    }
                }
            },
            modifier = Modifier
                .weight(0.9f)
                .background(MaterialTheme.colorScheme.primary)
        )

        AppButton(
            onClick = onSubmit,
            isLoading = isLoading,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Submit Answer",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }



    }
}
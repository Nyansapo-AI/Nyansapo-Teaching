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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.common.components.AppButton
import com.nyansapoai.teaching.presentation.common.components.AppTouchInput

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NumeracyAddition(
    modifier: Modifier = Modifier,
    firstNumber: Int,
    secondNumber: Int,
//    isEraserMode: Boolean = false,
) {

    var isEraserMode by remember { mutableStateOf(false) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        item {
            Text(
                text = "Addition",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        item {
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
                Modifier,
                /*
                ButtonGroupDefaults.ExpandedRatio,
                ButtonGroupDefaults.HorizontalArrangement,
               `
                 */
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
                                    .background(if (!isEraserMode) MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f) else Color.Transparent)
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
                                    .background(if (isEraserMode) MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f) else Color.Transparent)
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
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                VerticalOperationItem(
                    operationSymbol = "+",
                    firstNumber = firstNumber,
                    secondNumber = secondNumber
                )

                Box(
                    modifier = Modifier
//                        .fillParentMaxWidth()
                        .widthIn(max = 200.dp, min = 100.dp)
                        .heightIn(min = 100.dp, max = 150.dp)
                        .background(MaterialTheme.colorScheme.tertiary)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                        )
                ) {
                    AppTouchInput(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.tertiary),
                        isEraserMode = isEraserMode,
                    )
                }
            }
        }


        item {
            Box(
                modifier = Modifier
                    .widthIn(max = 420.dp, min = 100.dp)
                    .heightIn(min = 400.dp, max = 600.dp)
                    .padding(horizontal = 12.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                    )
            ) {
                AppTouchInput(
                    isEraserMode = isEraserMode,
                    brushColor = Color.Green
                )
            }
        }

        item {
            AppButton(
                onClick = {}
            ) { }
        }




    }
}



@Composable
fun VerticalOperationItem(
    operationSymbol: String,
    firstNumber: Int,
    secondNumber: Int,
){
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = operationSymbol,
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold
        )

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "$firstNumber",
                style = MaterialTheme.typography.displayLarge,
                letterSpacing = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$secondNumber",
                style = MaterialTheme.typography.displayLarge,
                letterSpacing = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NumeracyAdditionPreview() {
    NumeracyAddition(
        firstNumber = 5,
        secondNumber = 1234342,
        modifier = Modifier.fillMaxWidth()
    )
}
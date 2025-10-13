package com.nyansapoai.teaching.presentation.assessments.numeracy.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyOperations
import com.nyansapoai.teaching.presentation.common.components.AppLinearProgressIndicator

@Composable
fun NumeracyArithmeticOperationsContainerUI(
    modifier: Modifier = Modifier,
    numeracyOperationList: List<NumeracyOperations> = emptyList(),
    title: String = "Numeracy Operations",
    currentIndex: Int = 0,
    onAnswerFilePathChange: (String) -> Unit,
    onWorkOutFilePathChange: (String) -> Unit,
    shouldCapture: Boolean,
    isLoading: Boolean = false,
    onIsSubmittingChange: (Boolean) -> Unit,
    onSubmit: () -> Unit = {  },
) {
    LaunchedEffect(shouldCapture) {
        onSubmit.invoke()
        Log.d("NumeracyOperationContainer", "NumeracyOperationContainerUI: shouldCapture = $shouldCapture")
    }


    if (numeracyOperationList.isEmpty()){
        Text(
            text= "Questions are not available",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        return
    }

    if (currentIndex >= numeracyOperationList.size) {
        Text(
            text = "No more questions available",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        return
    }

    val numeracyOperation = numeracyOperationList[currentIndex]
    val orientation = when(numeracyOperation.operationType){
        OperationType.ADDITION ,
        OperationType.SUBTRACTION -> Orientation.Vertical
        OperationType.MULTIPLICATION,
        OperationType.DIVISION -> Orientation.Horizontal
    }

    var progress by remember {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(currentIndex) {
        progress = if (currentIndex < numeracyOperationList.size) {
            (currentIndex + 1).toFloat() / numeracyOperationList.size.toFloat()
        } else {
            1f
        }
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    )
    {

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Text(
                text = "Question ${currentIndex + 1}/${numeracyOperationList.size}",
                style = MaterialTheme.typography.titleMedium,
            )
            AppLinearProgressIndicator(
                progress = progress
            )
        }

        when(orientation){
            Orientation.Horizontal -> {
                NumeracyHorizontalArithmeticOperationsUI(
                    firstNumber = numeracyOperation.firstNumber,
                    secondNumber = numeracyOperation.secondNumber,
                    operationType = numeracyOperation.operationType ,
                    onSubmit = {
                        onIsSubmittingChange(true)
                    },
                    isLoading = isLoading,
                    shouldCapture = shouldCapture,
                    onAnswerFilePathChange = onAnswerFilePathChange,
                    onWorkAreaFilePathChange = onWorkOutFilePathChange,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
            Orientation.Vertical -> {
                NumeracyVerticalArithmeticOperationUI(
                    firstNumber = numeracyOperation.firstNumber,
                    secondNumber = numeracyOperation.secondNumber,
                    operationType = numeracyOperation.operationType ,
                    onSubmit = {
                        onIsSubmittingChange(true)
                    },
                    isLoading = isLoading,
                    shouldCapture = shouldCapture,
                    onAnswerFilePathChange = onAnswerFilePathChange,
                    onWorkAreaFilePathChange = onWorkOutFilePathChange,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                )

            }
        }


    }
}
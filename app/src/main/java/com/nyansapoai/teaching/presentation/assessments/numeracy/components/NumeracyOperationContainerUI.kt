package com.nyansapoai.teaching.presentation.assessments.numeracy.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyOperations
import com.nyansapoai.teaching.presentation.common.components.AppLinearProgressIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumeracyOperationContainerUI(
    modifier: Modifier = Modifier,
    numeracyOperationList: List<NumeracyOperations> = emptyList(),
    currentIndex: Int = 0,
    onAnswerFilePathChange: (String) -> Unit,
    onWorkOutFilePathChange: (String) -> Unit,
    shouldCapture: Boolean,
    isLoading: Boolean = false,
    onSubmit: () -> Unit = {  },
    )
{

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


    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp),
        modifier = modifier
            .widthIn(max = 700.dp)
            .padding(16.dp),
    ) {

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
            )
            {
                Text(
                    text = "Question ${currentIndex + 1}/${numeracyOperationList.size}",
                    style = MaterialTheme.typography.titleMedium,
                )

                AppLinearProgressIndicator(
                    progress = progress
                )
            }
        }

        item {
            NumeracyOperationUI(
                firstNumber = numeracyOperation.firstNumber,
                secondNumber = numeracyOperation.secondNumber,
                operationType = numeracyOperation.operationType ,
                operationOrientation = orientation,
                onSubmit = onSubmit,
                isLoading = isLoading,
                shouldCapture = shouldCapture,
                onAnswerFilePathChange = onAnswerFilePathChange,
                onWorkAreaFilePathChange = onWorkOutFilePathChange
            )

        }

    }


}
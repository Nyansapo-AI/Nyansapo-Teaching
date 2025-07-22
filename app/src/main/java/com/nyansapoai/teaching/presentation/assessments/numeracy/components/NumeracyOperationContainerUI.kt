package com.nyansapoai.teaching.presentation.assessments.numeracy.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyOperations
import com.nyansapoai.teaching.presentation.common.components.AppLinearProgressIndicator
import com.nyansapoai.teaching.utils.Utils.saveToImageFile

@Composable
fun NumeracyOperationContainerUI(
    modifier: Modifier = Modifier,
    numeracyOperationList: List<NumeracyOperations>,
    currentIndex: Int,
    answerImageBitmap: ImageBitmap?,
    onChangeAnswerBitmap: (ImageBitmap) -> Unit,
    onAnswerFilePathChange: (String) -> Unit,
    workAreaImageBitmap: ImageBitmap? ,
    onChangeWorkOutImageBitmap: (ImageBitmap) -> Unit,
    onWorkOutFilePathChange: (String) -> Unit = {},
    isSubmitting: Boolean = false,
    changeIsSubmitting: (Boolean) -> Unit = {  },
    isLoading: Boolean = false,
    onConfirmSubmit: () -> Unit = {  },
    ) {

    val context = LocalContext.current

    LaunchedEffect(isSubmitting) {
        answerImageBitmap?.let { imageBitmap ->
            imageBitmap.saveToImageFile(
                context = context,
                filename = null
            ).also { file ->
                file?.let { onAnswerFilePathChange(file.absolutePath)  }
            }
        }

        workAreaImageBitmap?.let { imageBitmap ->
            imageBitmap.saveToImageFile(
                context = context,
                filename = null
            ).also { file ->
                file?.let { onWorkOutFilePathChange(file.absolutePath)  }
            }
        }
        onConfirmSubmit.invoke()
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


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp),
        modifier = modifier
            .widthIn(max = 700.dp)
            .padding(16.dp),
    ) {
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

        NumeracyOperationUI(
            firstNumber = numeracyOperation.firstNumber,
            secondNumber = numeracyOperation.secondNumber,
            operationType = numeracyOperation.operationType ,
            operationOrientation = orientation,
//            shouldCaptureWorkArea = TODO(),
            onSubmit = {
                changeIsSubmitting(true)
            },
            isLoading = isLoading,
            onCaptureAnswerImageBitmap = onChangeAnswerBitmap,
            onCaptureWorkAreaImageBitmap = onChangeWorkOutImageBitmap
        )
    }


}
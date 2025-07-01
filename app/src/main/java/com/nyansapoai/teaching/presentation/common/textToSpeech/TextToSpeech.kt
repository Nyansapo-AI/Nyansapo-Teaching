package com.nyansapoai.teaching.presentation.common.textToSpeech

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nyansapoai.teaching.presentation.common.permissions.RequestAppPermissions
import org.koin.androidx.compose.koinViewModel

@Composable
fun TextToSpeechRoot() {

    val viewModel = koinViewModel<TextToSpeechViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    var isPermissionsAllowed by remember { mutableStateOf(false) }


    RequestAppPermissions(
        permissionsArray = arrayOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.RECORD_AUDIO
        ),
        onSuccess = {
            isPermissionsAllowed = true
        }
    )

    // Only attempt to speak when permissions are granted
    LaunchedEffect(isPermissionsAllowed) {
        if (isPermissionsAllowed) {
            // Make sure there's text to speak
            viewModel.updateText("Hello, welcome to the application")
            viewModel.speakText()
        }
    }

    if (isPermissionsAllowed){
        TextToSpeechScreen(
            state = state,
//        onAction = viewModel::onAction
        )

    }
}

@Composable
fun TextToSpeechScreen(
    state: TextToSpeechState,
//    onAction: (TextToSpeechAction) -> Unit,
) {

    Column {
        Text(
            text = state.text
        )

        Text(
            text = state.error ?: "No errors",
        )
    }

}
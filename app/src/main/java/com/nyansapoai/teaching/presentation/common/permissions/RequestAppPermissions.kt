package com.nyansapoai.teaching.presentation.common.permissions

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun RequestAppPermissions(
    modifier: Modifier = Modifier,
    permissionsArray: Array<String>,
    onSuccess: () -> Unit,
    onFailure: () -> Unit = {},
    content: (@Composable (() -> Unit ) -> Unit)? = null,
    isButtonClicked: Boolean = false
) {
    val context = LocalContext.current

    var isRequestingPermissions by remember { mutableStateOf(false) }

    // Check if all permissions are already granted
    val allPermissionsGranted = remember(permissionsArray) {
        permissionsArray.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }

        if (allGranted) {
            onSuccess.invoke()
            isRequestingPermissions = false
        } else {
//            onFailure.invoke()

            when(isButtonClicked){
                true -> {
                    isRequestingPermissions = true
                }
                false -> {
                    onFailure.invoke()
                }
            }
        }
    }


    LaunchedEffect(key1 = isButtonClicked) {
        if (allPermissionsGranted) {
            onSuccess.invoke()
        } else if (!isRequestingPermissions) {
            isRequestingPermissions = true
//            requestPermissionLauncher.launch(permissionsArray)
        }
    }

    AnimatedVisibility(
        visible = isRequestingPermissions
    ) {
        Box(modifier = modifier) {
            content?.invoke(
                { requestPermissionLauncher.launch(input = permissionsArray) }
            )
        }
    }


}
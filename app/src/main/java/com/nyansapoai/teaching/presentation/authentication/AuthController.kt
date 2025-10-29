package com.nyansapoai.teaching.presentation.authentication

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.presentation.getStarted.GetStartedRoot
import com.nyansapoai.teaching.presentation.home.HomeRoot
import com.nyansapoai.teaching.presentation.onboarding.OnboardingRoot
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthControllerRoot() {

    val viewModel = koinViewModel<AuthControllerViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    AuthControllerScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun AuthControllerScreen(
    state: AuthControllerState,
    onAction: (AuthControllerAction) -> Unit,
) {

    when(state.isUserLoggedIn){
        false -> {
            GetStartedRoot()
        }
        true -> {
            OnboardingRoot()
        }
    }

}
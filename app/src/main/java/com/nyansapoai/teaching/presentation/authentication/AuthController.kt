package com.nyansapoai.teaching.presentation.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nyansapoai.teaching.presentation.getStarted.GetStartedRoot
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
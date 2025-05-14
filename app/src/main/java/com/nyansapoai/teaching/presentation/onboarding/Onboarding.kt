package com.nyansapoai.teaching.presentation.onboarding

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.presentation.common.components.StepContent
import com.nyansapoai.teaching.presentation.common.components.StepsRow
import com.nyansapoai.teaching.presentation.onboarding.components.SelectOrganization
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingRoot() {

    val viewModel = koinViewModel<OnboardingViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    OnboardingScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun OnboardingScreen(
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit = {},
) {


    val stepState = rememberLazyListState()


    LaunchedEffect(state.currentStep){
        stepState.animateScrollToItem(index = state.currentStep)
    }

    val onboardingSteps = remember {
        listOf(
            StepContent(
                screen = { SelectOrganization() },
                onSubmit = {},
                title = "Organization"
            ),
            StepContent(
                screen = { SelectOrganization() },
                onSubmit = {},
                title = "Project"
            ),
            StepContent(
                screen = { SelectOrganization() },
                onSubmit = {},
                title = "School"
            ),
            StepContent(
                screen = { SelectOrganization() },
                onSubmit = {},
                title = "Camp"
            )
        )
    }

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .statusBarsPadding()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
//                .padding(start = 16.dp, end = 16.dp)
        ) {
            item {
                StepsRow(
                    state = stepState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    numberOfSteps = onboardingSteps.size,
                    currentStep = state.currentStep,
                    stepDescriptionList = onboardingSteps,
                    onClick = { step ->
                        onAction.invoke(OnboardingAction.OnStepChange(step = step))
                    }
                )
            }


            item{
                onboardingSteps[state.currentStep].screen
            }
        }
    }
}


/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewOnboarding(){
//    OnboardingScreen()
} */
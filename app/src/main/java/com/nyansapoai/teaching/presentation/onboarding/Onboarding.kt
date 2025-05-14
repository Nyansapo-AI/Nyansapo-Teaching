package com.nyansapoai.teaching.presentation.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.common.components.AppButton
import com.nyansapoai.teaching.presentation.common.components.StepContent
import com.nyansapoai.teaching.presentation.common.components.StepsRow
import com.nyansapoai.teaching.presentation.onboarding.components.SelectOrganization
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingRoot() {

    val viewModel = koinViewModel<OnboardingViewModel>()
    val state by viewModel.state.collectAsState()

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
                screen = {
                    SelectOrganization(
                        organizationList = listOf(
                            OrganizationUI(
                                name = "Nyansapo",
                                id = "1"
                            ),
                            OrganizationUI(
                                name = "Organization 1",
                                id = "3"
                            ),
                            OrganizationUI(
                                name = "Organization 2",
                                id = "3"
                            ),
                            OrganizationUI(
                                name = "Team Name",
                                id = "3"
                            )
                        ),
                        selectedOrganization = state.selectedOrganization,
                        onSelectOrganization = { organization ->
                            onAction.invoke(OnboardingAction.OnSelectOrganization(organizationUI = organization))
                        }
                    )
                },
                onSubmit = {},
                title = "Organization"
            ),
            StepContent(
                screen = {  },
                onSubmit = {},
                title = "Project"
            ),
            StepContent(
                screen = {  },
                onSubmit = {},
                title = "School"
            ),
            StepContent(
                screen = {  },
                onSubmit = {},
                title = "Camp"
            )
        )
    }

    Scaffold(
        topBar = {
            /*
            TopAppBar(

            ) */
        },
        modifier = Modifier
            .navigationBarsPadding()
            .statusBarsPadding()
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(30.dp),
                modifier = Modifier
                    .padding(innerPadding)
            ) {

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

                onboardingSteps[state.currentStep - 1 ].screen(Modifier)

            }


            AppButton(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.9f)
            ) {
                Text(
                    text = stringResource(R.string.next),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

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
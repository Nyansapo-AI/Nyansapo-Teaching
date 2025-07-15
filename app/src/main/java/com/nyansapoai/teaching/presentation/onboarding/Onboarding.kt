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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.navigation.HomePage
import com.nyansapoai.teaching.presentation.common.components.AppButton
import com.nyansapoai.teaching.presentation.common.components.StepContent
import com.nyansapoai.teaching.presentation.common.components.StepsRow
import com.nyansapoai.teaching.presentation.onboarding.components.SelectCamp
import com.nyansapoai.teaching.presentation.onboarding.components.SelectOrganization
import com.nyansapoai.teaching.presentation.onboarding.components.SelectProject
import com.nyansapoai.teaching.presentation.onboarding.components.SelectSchool
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

    LaunchedEffect(state.currentStep) {
        stepState.animateScrollToItem(index = state.currentStep)
    }

    val onboardingSteps = listOf(
        /*
        StepContent(
            screen = {
                SelectOrganization(
                    organizationList = state.userData?.organizations ?: emptyList(),
                    selectedOrganization = state.selectedOrganization,
                    onSelectOrganization = { organization ->
                        onAction(OnboardingAction.OnSelectOrganization(organizationUI = organization))
                    }
                )
            },
            onSubmit = {},
            title = "Organization"
        ),
        StepContent(
            screen = {
                SelectProject(
                    projectList = state.selectedOrganization?.projects ?: emptyList(),
                    selectedProject = state.selectedProject,
                    onSelectProject = { project ->
                        onAction.invoke(OnboardingAction.OnSelectProject(project = project))
                    }
                )
            },
            onSubmit = {},
            title = "Project"
        ),*/
        StepContent(
            screen = {
                SelectSchool(
                    schoolList = state.selectedProject?.schools ?: emptyList(),
                    selectedSchool = state.selectedSchool,
                    onSelectSchool = {school ->
                        onAction.invoke(OnboardingAction.OnSelectSchool(school = school))
                    }
                )
            },
            onSubmit = {},
            title = "School"
        ),
        /*
        StepContent(
            screen = {
                SelectCamp(
                    campList = listOf(
                        OnboardingCampState(name = "Camp One", id = "1"),
                        OnboardingCampState(name = "Camp Name 2", id = "1"),
                        OnboardingCampState(name = "Camp Two", id = "1"),
                        OnboardingCampState(name = "Camp Four", id = "1"),
                        OnboardingCampState(name = "Camp G", id = "1"),
                    ),
                    selectedCamp = state.selectedCamp,
                    onSelectCamp = {camp ->
                        onAction.invoke(OnboardingAction.OnSelectCamp(camp = camp))
                    }
                )
            },
            onSubmit = {},
            title = "Camp"
        )*/
    )

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .statusBarsPadding()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(30.dp),
                modifier = Modifier.padding(innerPadding)
            ) {
                /*
                StepsRow(
                    state = stepState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    numberOfSteps = onboardingSteps.size,
                    currentStep = state.currentStep,
                    stepDescriptionList = onboardingSteps,
                    onClick = { step ->
                        onAction(OnboardingAction.OnStepChange(step = step))
                    }
                )*/

                onboardingSteps[state.currentStep - 1].screen(Modifier)
            }


            AppButton(
                enabled = state.selectedOrganization != null && state.selectedProject != null && state.selectedSchool != null ,
                onClick = {
//                    throw RuntimeException("Test Crash")

                    onAction.invoke(OnboardingAction.OnContinue(
                        onSuccess = {
                            navController.navigate(HomePage)
                        }
                    ))
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.9f)
            ) {
                Text(
                    text = stringResource(R.string.continue_text),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
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
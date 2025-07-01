package com.nyansapoai.teaching.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.presentation.assessments.IndividualAssessment.IndividualAssessmentRoot
import com.nyansapoai.teaching.presentation.assessments.conductAssessment.ConductAssessmentRoot
import com.nyansapoai.teaching.presentation.assessments.createAssessment.CreateAssessmentsRoot
import com.nyansapoai.teaching.presentation.authentication.otp.OTPRoot
import com.nyansapoai.teaching.presentation.authentication.signIn.SignInRoot
import com.nyansapoai.teaching.presentation.common.snackbar.SnackBarHandler
import com.nyansapoai.teaching.presentation.getStarted.GetStartedRoot
import com.nyansapoai.teaching.presentation.home.HomeRoot
import com.nyansapoai.teaching.presentation.onboarding.OnboardingRoot
import com.nyansapoai.teaching.utils.ResultStatus
import org.koin.compose.koinInject

@Composable
fun Navigation(){
    navController = rememberNavController()

    val snackBarHandler = koinInject<SnackBarHandler>()
    val snackBarNotification by snackBarHandler.snackBarNotification.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackBarNotification.status) {
        if (snackBarNotification.status == ResultStatus.SUCCESS && snackBarNotification.data != null) {
            snackBarHostState.showSnackbar(
                duration = snackBarNotification.data?.duration ?: SnackbarDuration.Long,
                message = snackBarNotification.data?.message ?: "",
            )
        }
    }

    Scaffold(
        snackbarHost = {
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomePage,
            modifier = Modifier
                .padding(innerPadding)
        ){

            composable<GetStartedPage> {
                GetStartedRoot()
            }

            composable<SignInPage> {
                SignInRoot()
            }

            composable<OTPPage> { backEntry ->
                val args = backEntry.toRoute<OTPPage>()
                OTPRoot(phoneNumber = args.phoneNumber)
            }


            composable<OnboardingPage> {
                OnboardingRoot()
            }

            composable<HomePage> {
                HomeRoot()
            }

            composable<CreateAssessmentsPage> {
                CreateAssessmentsRoot()
            }

            composable<IndividualAssessmentPage> {
                val args = it.toRoute<IndividualAssessmentPage>()
                IndividualAssessmentRoot(assessmentId = args.assessmentId)
            }

            composable<ConductAssessmentPage> {
                val args = it.toRoute<ConductAssessmentPage>()
                 ConductAssessmentRoot(assessmentId = args.assessmentId, studentId = args.studentId, assessmentType = args.assessmentType, assessmentNo = args.assessmentNo)
            }

        }


    }



}
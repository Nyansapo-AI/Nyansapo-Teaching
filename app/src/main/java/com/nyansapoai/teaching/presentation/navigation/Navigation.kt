package com.nyansapoai.teaching.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.presentation.authentication.otp.OTPRoot
import com.nyansapoai.teaching.presentation.authentication.signIn.SignInRoot
import com.nyansapoai.teaching.presentation.getStarted.GetStartedRoot
import com.nyansapoai.teaching.presentation.onboarding.OnboardingRoot

@Composable
fun Navigation(){
    navController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = OnboardingPage,
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

        }


    }



}
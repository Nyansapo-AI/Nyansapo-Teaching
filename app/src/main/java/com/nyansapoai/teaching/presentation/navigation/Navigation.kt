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
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.presentation.authentication.otp.OTPRoot
import com.nyansapoai.teaching.presentation.authentication.signIn.SignInRoot
import com.nyansapoai.teaching.presentation.getStarted.GetStartedRoot

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
            startDestination = GetStartedPage,
            modifier = Modifier
                .padding(innerPadding)
        ){
            composable<GetStartedPage> {
                GetStartedRoot()
            }

            composable<SignInPage> {
                SignInRoot()
            }

            composable<OTPPage> {
                OTPRoot()
            }

        }


    }



}
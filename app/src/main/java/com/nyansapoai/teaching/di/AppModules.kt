package com.nyansapoai.teaching.di

import com.google.firebase.auth.FirebaseAuth
import com.nyansapoai.teaching.data.remote.authentication.AuthenticationRepository
import com.nyansapoai.teaching.data.remote.authentication.AuthenticationRepositoryImp
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.nyansapoai.teaching.presentation.onboarding.OnboardingViewModel
import com.nyansapoai.teaching.presentation.getStarted.GetStartedViewModel
import com.nyansapoai.teaching.presentation.authentication.signIn.SignInViewModel
import com.nyansapoai.teaching.presentation.authentication.otp.OTPViewModel
import com.nyansapoai.teaching.presentation.home.HomeViewModel

val appModules = module {

    viewModelOf(::OnboardingViewModel)
    viewModelOf(::GetStartedViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::OTPViewModel)
    viewModelOf(::HomeViewModel)


    single<FirebaseAuth> {
        FirebaseAuth.getInstance()
    }

    single<AuthenticationRepository> {
        AuthenticationRepositoryImp(
            auth = get()
        )
    }

}
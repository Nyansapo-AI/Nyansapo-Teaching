package com.nyansapoai.teaching.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.nyansapoai.teaching.presentation.onboarding.OnboardingViewModel

val appModules = module {
    viewModelOf(::OnboardingViewModel)
}
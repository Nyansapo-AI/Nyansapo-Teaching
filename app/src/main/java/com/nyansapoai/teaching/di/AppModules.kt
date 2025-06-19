package com.nyansapoai.teaching.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nyansapoai.teaching.data.azure.ai.AzureArtificialIntelligenceRepositoryImp
import com.nyansapoai.teaching.data.firebase.assessment.AssessmentRepositoryFirebaseImp
import com.nyansapoai.teaching.data.firebase.media.FirebaseMediaRepositoryImpl
import com.nyansapoai.teaching.data.network.ApiHelper
import com.nyansapoai.teaching.data.remote.ai.ArtificialIntelligenceRepository
import com.nyansapoai.teaching.data.remote.ai.OnlineArtificialIntelligenceRepositoryImp
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.data.remote.authentication.AuthenticationRepository
import com.nyansapoai.teaching.data.remote.authentication.AuthenticationRepositoryImp
import com.nyansapoai.teaching.data.remote.media.MediaRepository
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.nyansapoai.teaching.presentation.onboarding.OnboardingViewModel
import com.nyansapoai.teaching.presentation.getStarted.GetStartedViewModel
import com.nyansapoai.teaching.presentation.authentication.signIn.SignInViewModel
import com.nyansapoai.teaching.presentation.authentication.otp.OTPViewModel
import com.nyansapoai.teaching.presentation.home.HomeViewModel
import com.nyansapoai.teaching.presentation.camps.CampViewModel
import com.nyansapoai.teaching.presentation.assessments.AssessmentsViewModel
import com.nyansapoai.teaching.presentation.assessments.createAssessment.CreateAssessmentsViewModel
import com.nyansapoai.teaching.presentation.assessments.IndividualAssessment.IndividualAssessmentViewModel
import com.nyansapoai.teaching.presentation.assessments.conductAssessment.ConductAssessmentViewModel
import com.nyansapoai.teaching.presentation.assessments.numeracy.NumeracyAssessmentViewModel
import com.nyansapoai.teaching.presentation.common.textToSpeech.TextToSpeechViewModel
import com.nyansapoai.teaching.presentation.common.snackbar.SnackBarHandler
import org.koin.core.module.dsl.viewModel

val appModules = module {

    viewModelOf(::OnboardingViewModel)
    viewModelOf(::GetStartedViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::OTPViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::CampViewModel)
    viewModelOf(::AssessmentsViewModel)
    viewModelOf(::CreateAssessmentsViewModel)
    viewModelOf(::IndividualAssessmentViewModel)
    viewModelOf(::ConductAssessmentViewModel)
    viewModelOf(::NumeracyAssessmentViewModel)
    viewModelOf(::TextToSpeechViewModel)


    single<FirebaseAuth> {
        FirebaseAuth.getInstance()
    }

    single<FirebaseFirestore> {
        FirebaseFirestore.getInstance()
    }

    single<FirebaseStorage> {
        FirebaseStorage.getInstance()
    }

    single<AuthenticationRepository> {
        AuthenticationRepositoryImp(
            auth = get()
        )
    }

    single<AssessmentRepository> {
        AssessmentRepositoryFirebaseImp(
            firebaseDb = get()
        )
    }

    single<SnackBarHandler> {
        SnackBarHandler()
    }

    single<ApiHelper> {
        ApiHelper()
    }

    factory<ArtificialIntelligenceRepository> {
        OnlineArtificialIntelligenceRepositoryImp(
            apiHelper = get(),
        )
    }

    factory<ArtificialIntelligenceRepository>{
        AzureArtificialIntelligenceRepositoryImp(
            apiHelper = get(),
        )
    }

    factory<MediaRepository> {
        FirebaseMediaRepositoryImpl(
            firebaseStorage = get()
        )
    }

}
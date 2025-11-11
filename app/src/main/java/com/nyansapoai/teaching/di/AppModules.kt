package com.nyansapoai.teaching.di

import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nyansapoai.teaching.Database
import com.nyansapoai.teaching.data.azure.ai.AzureArtificialIntelligenceRepositoryImp
import com.nyansapoai.teaching.data.firebase.assessment.AssessmentRepositoryFirebaseImp
import com.nyansapoai.teaching.data.firebase.attendance.FirebaseAttendanceRepositoryImp
import com.nyansapoai.teaching.data.firebase.media.FirebaseMediaRepositoryImpl
import com.nyansapoai.teaching.data.firebase.schools.SchoolRepositoryFirebaseImp
import com.nyansapoai.teaching.data.firebase.students.StudentsRepositoryFirebaseImp
import com.nyansapoai.teaching.data.firebase.user.UserRepositoryFirebaseImp
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.local.LocalDatabaseDriverFactory
import com.nyansapoai.teaching.data.local.sqldelight.SQLDelightDataSourceImp
import com.nyansapoai.teaching.data.network.ApiHelper
import com.nyansapoai.teaching.data.remote.ai.ArtificialIntelligenceRepository
import com.nyansapoai.teaching.data.remote.ai.OnlineArtificialIntelligenceRepositoryImp
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.data.remote.attendance.AttendanceRepository
import com.nyansapoai.teaching.data.remote.authentication.AuthenticationRepository
import com.nyansapoai.teaching.data.remote.authentication.AuthenticationRepositoryImp
import com.nyansapoai.teaching.data.remote.media.MediaRepository
import com.nyansapoai.teaching.data.remote.school.SchoolRepository
import com.nyansapoai.teaching.data.remote.students.StudentsRepository
import com.nyansapoai.teaching.data.remote.user.UserRepository
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.nyansapoai.teaching.presentation.onboarding.OnboardingViewModel
import com.nyansapoai.teaching.presentation.getStarted.GetStartedViewModel
import com.nyansapoai.teaching.presentation.authentication.signIn.SignInViewModel
import com.nyansapoai.teaching.presentation.authentication.AuthControllerViewModel
import com.nyansapoai.teaching.presentation.authentication.otp.OTPViewModel
import com.nyansapoai.teaching.presentation.home.HomeViewModel
import com.nyansapoai.teaching.presentation.schools.SchoolViewModel
import com.nyansapoai.teaching.presentation.assessments.AssessmentsViewModel
import com.nyansapoai.teaching.presentation.assessments.createAssessment.CreateAssessmentsViewModel
import com.nyansapoai.teaching.presentation.assessments.IndividualAssessment.IndividualAssessmentViewModel
import com.nyansapoai.teaching.presentation.assessments.conductAssessment.ConductAssessmentViewModel
import com.nyansapoai.teaching.presentation.assessments.literacy.LiteracyViewModel
import com.nyansapoai.teaching.presentation.assessments.assessmentResult.literacyResult.LiteracyResultViewModel
import com.nyansapoai.teaching.presentation.assessments.numeracy.NumeracyAssessmentViewModel
import com.nyansapoai.teaching.presentation.assessments.assessmentResult.numeracyResults.NumeracyAssessmentResultViewModel
import com.nyansapoai.teaching.presentation.attendances.AttendancesViewModel
import com.nyansapoai.teaching.presentation.attendances.collectAttendance.CollectAttendanceViewModel
import com.nyansapoai.teaching.presentation.common.audio.play.AndroidAudioPlayer
import com.nyansapoai.teaching.presentation.common.audio.play.AudioPlayer
import com.nyansapoai.teaching.presentation.common.audio.record.AndroidAppAudioRecorder
import com.nyansapoai.teaching.presentation.common.audio.record.AppAudioRecorder
import com.nyansapoai.teaching.presentation.common.snackbar.SnackBarHandler
import com.nyansapoai.teaching.presentation.students.StudentsViewModel

val appModules = module {

    viewModelOf(::OnboardingViewModel)
    viewModelOf(::AuthControllerViewModel)
    viewModelOf(::GetStartedViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::OTPViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::SchoolViewModel)
    viewModelOf(::AssessmentsViewModel)
    viewModelOf(::CreateAssessmentsViewModel)
    viewModelOf(::IndividualAssessmentViewModel)
    viewModelOf(::ConductAssessmentViewModel)
    viewModelOf(::NumeracyAssessmentViewModel)
    viewModelOf(::LiteracyViewModel)
    viewModelOf(::StudentsViewModel)
    viewModelOf(::LiteracyResultViewModel)
    viewModelOf(::NumeracyAssessmentResultViewModel)
    viewModelOf(::AttendancesViewModel)
    viewModelOf(::CollectAttendanceViewModel)



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
            auth = get(),
            firebaseDb = get()
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

    factory<AppAudioRecorder> {
        AndroidAppAudioRecorder(
            context = get()
        )
    }

    factory<AudioPlayer> {
        AndroidAudioPlayer(
            context = get()
        )
    }

    single<Database> {
        Database(driver = get<LocalDatabaseDriverFactory>().create())
    }

    single<LocalDatabaseDriverFactory> {
        LocalDatabaseDriverFactory(context = get())
    }

    single<LocalDataSource> {
        SQLDelightDataSourceImp(
            database = get<Database>()
        )
    }

    single<UserRepository> {
        UserRepositoryFirebaseImp(
            auth = get(),
            firebaseDb = get()
        )
    }

    single<WorkManager> {
        WorkManager.getInstance(context = get())
    }

    single<SchoolRepository> {
        SchoolRepositoryFirebaseImp(
            localDataSource = get(),
            firebaseDb = get()
        )
    }

    single<StudentsRepository> {
        StudentsRepositoryFirebaseImp(
            firebaseDb = get(),
        )
    }

    single<AttendanceRepository> {
        FirebaseAttendanceRepositoryImp(
            firebaseDb = get(),
        )
    }

}
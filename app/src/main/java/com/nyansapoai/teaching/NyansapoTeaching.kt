package com.nyansapoai.teaching

import android.app.Application
import androidx.startup.AppInitializer
import app.rive.runtime.kotlin.RiveInitializer
import app.rive.runtime.kotlin.core.Rive
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nyansapoai.teaching.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

private lateinit var auth: FirebaseAuth

class NyansapoTeaching: Application(){


    override fun onCreate() {
        super.onCreate()

        /*
        AppInitializer.getInstance(this@NyansapoTeaching)
            .initializeComponent(RiveInitializer::class.java)

         */

//        Rive.init(this@NyansapoTeaching)

        auth = Firebase.auth

        startKoin {
            androidLogger()
            androidContext(this@NyansapoTeaching)
            modules(    appModules)
        }
    }
}
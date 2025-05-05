package com.nyansapoai.teaching

import android.app.Application
import com.nyansapoai.teaching.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class NyansapoTeaching: Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@NyansapoTeaching)
            modules(appModules)
        }
    }
}
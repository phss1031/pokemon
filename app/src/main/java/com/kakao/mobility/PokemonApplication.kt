package com.kakao.mobility

import android.app.Application
import com.kakao.mobility.di.applicationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

class PokemonApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            initKoinApplication(this@PokemonApplication)
        }
    }

    private fun KoinApplication.initKoinApplication(application: Application) {
        androidContext(application)
        androidLogger()
        modules(applicationModule)
    }
}
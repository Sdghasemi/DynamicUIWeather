package com.hirno.weather

import android.app.Application
import android.content.Intent
import android.util.Log
import com.hirno.weather.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import kotlin.system.exitProcess


class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        setupDependencyInjection()
    }

    private fun setupDependencyInjection() {
        startKoin {
            androidLogger()
            androidContext(this@WeatherApplication)
            modules(
                appModule,
                module {
                    single { this@WeatherApplication }
                }
            )
        }
    }
}
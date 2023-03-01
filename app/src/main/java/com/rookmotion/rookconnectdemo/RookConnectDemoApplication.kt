package com.rookmotion.rookconnectdemo

import android.app.Application
import com.rookmotion.rookconnectdemo.di.ServiceLocator

class RookConnectDemoApplication : Application() {
    lateinit var serviceLocator: ServiceLocator

    override fun onCreate() {
        super.onCreate()

        serviceLocator = ServiceLocator(applicationContext)
    }
}
package com.rookmotion.rookconnectdemo.extension

import android.app.Activity
import com.rookmotion.rookconnectdemo.RookConnectDemoApplication
import com.rookmotion.rookconnectdemo.di.ServiceLocator

val Activity.serviceLocator: ServiceLocator get() = (application as RookConnectDemoApplication).serviceLocator

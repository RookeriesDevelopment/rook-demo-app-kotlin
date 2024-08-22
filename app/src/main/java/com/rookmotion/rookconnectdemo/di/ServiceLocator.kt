package com.rookmotion.rookconnectdemo.di

import android.content.Context
import com.rookmotion.rook.sdk.RookConfigurationManager
import com.rookmotion.rook.sdk.RookDataSources
import com.rookmotion.rook.sdk.RookPermissionsManager
import com.rookmotion.rook.sdk.RookStepsManager
import com.rookmotion.rookconnectdemo.common.isDebug
import com.rookmotion.rookconnectdemo.data.preferences.RookDemoPreferences

class ServiceLocator(context: Context) {

    val rookConfigurationManager: RookConfigurationManager by lazy {
        RookConfigurationManager(context)
    }

    val rookPermissionsManager: RookPermissionsManager by lazy {
        RookPermissionsManager(context)
    }

    val rookDemoPreferences: RookDemoPreferences by lazy {
        RookDemoPreferences(context)
    }

    val rookStepsManager: RookStepsManager by lazy {
        RookStepsManager(context)
    }

    val rookDataSources: RookDataSources by lazy {
        RookDataSources(context)
    }

    val connectionsPageUrl = if (isDebug) {
        "https://connections.rook-connect.review/"
    } else {
        "https://connections.rook-connect.com/"
    }
}
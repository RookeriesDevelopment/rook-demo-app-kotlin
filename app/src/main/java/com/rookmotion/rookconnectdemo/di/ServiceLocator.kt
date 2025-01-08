package com.rookmotion.rookconnectdemo.di

import android.content.Context
import com.rookmotion.rook.sdk.RookConfigurationManager
import com.rookmotion.rook.sdk.RookContinuousUploadManager
import com.rookmotion.rook.sdk.RookDataSources
import com.rookmotion.rook.sdk.RookEventManager
import com.rookmotion.rook.sdk.RookPermissionsManager
import com.rookmotion.rook.sdk.RookStepsManager
import com.rookmotion.rook.sdk.RookSummaryManager
import com.rookmotion.rookconnectdemo.data.preferences.RookDemoPreferences

class ServiceLocator(context: Context) {

    val rookDemoPreferences: RookDemoPreferences by lazy {
        RookDemoPreferences(context)
    }

    val rookConfigurationManager: RookConfigurationManager by lazy {
        RookConfigurationManager(context)
    }

    val rookSummaryManager: RookSummaryManager by lazy {
        RookSummaryManager(rookConfigurationManager)
    }

    val rookEventManager: RookEventManager by lazy {
        RookEventManager(rookConfigurationManager)
    }

    val rookPermissionsManager: RookPermissionsManager by lazy {
        RookPermissionsManager(context)
    }

    val rookStepsManager: RookStepsManager by lazy {
        RookStepsManager(context)
    }

    val rookDataSources: RookDataSources by lazy {
        RookDataSources(context)
    }

    val rookContinuousUploadManager: RookContinuousUploadManager by lazy {
        RookContinuousUploadManager(context)
    }
}

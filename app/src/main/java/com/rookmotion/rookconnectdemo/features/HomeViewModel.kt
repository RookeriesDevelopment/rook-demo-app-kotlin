package com.rookmotion.rookconnectdemo.features

import androidx.lifecycle.ViewModel
import com.rookmotion.rookconnectdemo.data.preferences.RookDemoPreferences

class HomeViewModel(
    private val rookDemoPreferences: RookDemoPreferences,
) : ViewModel() {

    val userAcceptedYesterdaySync: Boolean get() = rookDemoPreferences.getUserAcceptedYesterdaySync()
}
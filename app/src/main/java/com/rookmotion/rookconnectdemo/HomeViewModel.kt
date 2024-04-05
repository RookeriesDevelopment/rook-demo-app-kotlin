package com.rookmotion.rookconnectdemo

import androidx.lifecycle.ViewModel
import com.rookmotion.rookconnectdemo.data.preferences.RookDemoPreferences

class HomeViewModel(
    private val rookDemoPreferences: RookDemoPreferences,
) : ViewModel() {

    val userAcceptedYesterdaySync: Boolean get() = rookDemoPreferences.getUserAcceptedYesterdaySync()
}
package com.rookmotion.rookconnectdemo.data.preferences

import android.content.Context
import android.content.SharedPreferences

class RookDemoPreferences(context: Context) {

    private val preferences: SharedPreferences

    init {
        preferences = context.getSharedPreferences("rook_demo_preferences", Context.MODE_PRIVATE)
    }

    fun setUserAcceptedYesterdaySync(accepted: Boolean) {
        preferences.edit().putBoolean(ACCEPTED_YESTERDAY_SYNC_KEY, accepted).apply()
    }

    fun getUserAcceptedYesterdaySync(): Boolean {
        return preferences.getBoolean(ACCEPTED_YESTERDAY_SYNC_KEY, false)
    }
}

private const val ACCEPTED_YESTERDAY_SYNC_KEY = "ACCEPTED_YESTERDAY_SYNC"

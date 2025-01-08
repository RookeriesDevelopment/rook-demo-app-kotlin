package com.rookmotion.rookconnectdemo.data.preferences

import android.content.Context
import android.content.SharedPreferences

class RookDemoPreferences(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(
        /* name = */ "rook_demo_preferences",
        /* mode = */ Context.MODE_PRIVATE,
    )

    fun setUserAcceptedContinuousUpload(accepted: Boolean) {
        preferences.edit().putBoolean(ACCEPTED_CONTINUOUS_UPLOAD_KEY, accepted).apply()
    }

    fun getUserAcceptedContinuousUpload(): Boolean {
        return preferences.getBoolean(ACCEPTED_CONTINUOUS_UPLOAD_KEY, false)
    }
}

private const val ACCEPTED_CONTINUOUS_UPLOAD_KEY = "ACCEPTED_CONTINUOUS_UPLOAD"

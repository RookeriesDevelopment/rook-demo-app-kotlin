package com.rookmotion.rookconnectdemo.di

import android.content.Context
import com.rookmotion.rook.auth.AuthorizationProvider
import com.rookmotion.rook.health_connect.RookHealthConnectManager
import com.rookmotion.rook.transmission.RookTransmissionManager
import com.rookmotion.rook.users.RookUsersManager
import com.rookmotion.rookconnectdemo.BuildConfig

class ServiceLocator(context: Context) {

    val authorizationProvider: AuthorizationProvider by lazy {
        if (BuildConfig.DEBUG) {
            AuthorizationProvider(context, BuildConfig.ROOK_AUTH_URL, "ADVANCED")
        } else {
            AuthorizationProvider(context, BuildConfig.ROOK_AUTH_URL)
        }
    }

    val rookUsersManager: RookUsersManager by lazy {
        if (BuildConfig.DEBUG) {
            RookUsersManager(
                context,
                BuildConfig.ROOK_USERS_URL,
                BuildConfig.CLIENT_UUID,
                BuildConfig.CLIENT_PASSWORD,
                "ADVANCED"
            )
        } else {
            RookUsersManager(
                context,
                BuildConfig.ROOK_USERS_URL,
                BuildConfig.CLIENT_UUID,
                BuildConfig.CLIENT_PASSWORD,
            )
        }
    }

    val rookTransmissionManager: RookTransmissionManager by lazy {
        if (BuildConfig.DEBUG) {
            RookTransmissionManager(
                context,
                BuildConfig.ROOK_TRANSMISSION_URL,
                BuildConfig.USER_ID,
                BuildConfig.CLIENT_UUID,
                BuildConfig.CLIENT_PASSWORD,
                "ADVANCED",
            )
        } else {
            RookTransmissionManager(
                context,
                BuildConfig.ROOK_TRANSMISSION_URL,
                BuildConfig.USER_ID,
                BuildConfig.CLIENT_UUID,
                BuildConfig.CLIENT_PASSWORD
            )
        }
    }

    val rookHealthConnectManager: RookHealthConnectManager by lazy {
        if (BuildConfig.DEBUG) {
            RookHealthConnectManager(context, "ADVANCED")
        } else {
            RookHealthConnectManager(context)
        }
    }
}
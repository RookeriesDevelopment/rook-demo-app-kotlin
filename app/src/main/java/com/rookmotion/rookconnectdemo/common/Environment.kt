package com.rookmotion.rookconnectdemo.common

import com.rookmotion.rook.health_connect.domain.environment.RookHealthConnectEnvironment
import com.rookmotion.rook.sdk.domain.environment.RookEnvironment
import com.rookmotion.rook.transmission.domain.environment.RookTransmissionEnvironment
import com.rookmotion.rook.users.domain.environment.RookUsersEnvironment
import com.rookmotion.rookconnectdemo.BuildConfig

val rookEnvironment = if (BuildConfig.DEBUG) RookEnvironment.SANDBOX
else RookEnvironment.PRODUCTION

val rookHealthConnectEnvironment = if (BuildConfig.DEBUG) RookHealthConnectEnvironment.SANDBOX
else RookHealthConnectEnvironment.PRODUCTION

val rookTransmissionEnvironment = if (BuildConfig.DEBUG) RookTransmissionEnvironment.SANDBOX
else RookTransmissionEnvironment.PRODUCTION

val rookUsersEnvironment = if(BuildConfig.DEBUG) RookUsersEnvironment.SANDBOX
else RookUsersEnvironment.PRODUCTION

val isDebug = BuildConfig.DEBUG

const val USER_ID = "clientHC"

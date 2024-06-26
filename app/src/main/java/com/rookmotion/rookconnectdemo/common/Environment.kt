package com.rookmotion.rookconnectdemo.common

import com.rookmotion.rook.sdk.domain.environment.RookEnvironment
import com.rookmotion.rookconnectdemo.BuildConfig

val rookEnvironment = RookEnvironment.PRODUCTION

val isDebug = BuildConfig.DEBUG

const val USER_ID = "clientHC"

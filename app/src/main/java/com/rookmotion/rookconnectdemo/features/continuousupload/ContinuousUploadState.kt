package com.rookmotion.rookconnectdemo.features.continuousupload

data class ContinuousUploadState(
    val hasAndroidPermissions: Boolean = false,
    val hasHealthConnectPermissions: Boolean = false,
    val isContinuousUploadEnabled: Boolean = false,
)

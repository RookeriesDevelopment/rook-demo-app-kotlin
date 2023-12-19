package com.rookmotion.rookconnectdemo.features.modules.healthconnect.playground

sealed class UploadState {
    object Ready : UploadState()
    object Uploading : UploadState()
    class Error(val message: String) : UploadState()
    object Uploaded : UploadState()
}

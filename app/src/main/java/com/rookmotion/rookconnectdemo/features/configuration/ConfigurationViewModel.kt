package com.rookmotion.rookconnectdemo.features.configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.RookConfigurationManager
import com.rookmotion.rook.sdk.domain.model.RookConfiguration
import com.rookmotion.rookconnectdemo.BuildConfig
import com.rookmotion.rookconnectdemo.common.ConsoleOutput
import com.rookmotion.rookconnectdemo.common.isDebug
import com.rookmotion.rookconnectdemo.common.rookEnvironment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConfigurationViewModel(
    private val rookConfigurationManager: RookConfigurationManager,
) : ViewModel() {

    val configurationOutput = ConsoleOutput()
    val initializeOutput = ConsoleOutput()
    val updateUserOutput = ConsoleOutput()

    private val _enableNavigation = MutableStateFlow(false)
    val enableNavigation get() = _enableNavigation.asStateFlow()

    fun setConfiguration() {
        configurationOutput.set("Setting configuration...")

        val rookConfiguration = RookConfiguration(
            BuildConfig.CLIENT_UUID,
            BuildConfig.SECRET_KEY,
            rookEnvironment,
        )

        configurationOutput.appendMultiple(
            "Using configuration:",
            "Client UUID: ${rookConfiguration.clientUUID}",
            "Secret Key: ${rookConfiguration.secretKey}",
            "Environment: ${rookConfiguration.environment}",
            "APP IN DEVELOPMENT MODE: $isDebug"
        )

        if (isDebug) {
            rookConfigurationManager.enableLocalLogs()
        }

        rookConfigurationManager.setConfiguration(rookConfiguration)

        configurationOutput.append("Configuration set successfully")
    }

    fun initialize() {
        initializeOutput.set("Initializing...")

        viewModelScope.launch {
            rookConfigurationManager.initRook().fold(
                {
                    initializeOutput.append("SDK initialized successfully")
                    checkUserIDRegistered()
                },
                {
                    initializeOutput.appendError(it, "Error initializing SDK")
                }
            )
        }
    }

    private fun checkUserIDRegistered() {
        updateUserOutput.clear()

        viewModelScope.launch {
            val userID = rookConfigurationManager.getUserID()

            if (userID != null) {
                updateUserOutput.append("Found local userID $userID, you can skip step 3")
                _enableNavigation.emit(true)
            } else {
                updateUserOutput.append("Local userID not found, please set a userID")
            }
        }
    }

    fun updateUserID(userID: String) {
        updateUserOutput.set("Updating userID...")

        viewModelScope.launch {
            rookConfigurationManager.updateUserID(userID).fold(
                {
                    updateUserOutput.append("userID updated successfully")
                    _enableNavigation.emit(true)
                },
                {
                    updateUserOutput.appendError(it, "Error updating userID")
                }
            )
        }
    }
}

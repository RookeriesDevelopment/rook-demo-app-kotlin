package com.rookmotion.rookconnectdemo.features.usermanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.RookConfigurationManager
import com.rookmotion.rookconnectdemo.common.ConsoleOutput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserManagementViewModel(
    private val rookConfigurationManager: RookConfigurationManager,
) : ViewModel() {

    val syncUserTimeZoneOutput = ConsoleOutput()
    val clearUserOutput = ConsoleOutput()
    val deleteUserOutput = ConsoleOutput()

    private val _userID = MutableStateFlow("")
    val userID get() = _userID.asStateFlow()

    init {
        viewModelScope.launch {
            rookConfigurationManager.getUserID()?.let {
                _userID.emit(it)
            }
        }
    }

    fun syncUserTimeZone() {
        syncUserTimeZoneOutput.set("Syncing user timezone...")

        viewModelScope.launch {
            rookConfigurationManager.syncUserTimeZone().fold(
                {
                    syncUserTimeZoneOutput.append("User timezone synced successfully")
                },
                {
                    syncUserTimeZoneOutput.appendError(it, "Error syncing user timezone")
                }
            )
        }
    }

    fun clearUserID() {
        clearUserOutput.set("Clearing user ID...")

        viewModelScope.launch {
            rookConfigurationManager.clearUserID().fold(
                {
                    clearUserOutput.append("User ID cleared successfully")
                },
                {
                    clearUserOutput.appendError(it, "Error clearing user ID")
                }
            )
        }
    }

    fun deleteUserFromRook() {
        deleteUserOutput.set("Deleting user from Rook...")

        viewModelScope.launch {
            rookConfigurationManager.deleteUserFromRook().fold(
                {
                    deleteUserOutput.append("User deleted from Rook successfully")
                },
                {
                    deleteUserOutput.appendError(it, "Error deleting user from Rook")
                }
            )
        }
    }
}

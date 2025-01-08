package com.rookmotion.rookconnectdemo.features.permissions

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.RookPermissionsManager
import com.rookmotion.rook.sdk.domain.enums.HealthConnectAvailability
import com.rookmotion.rook.sdk.domain.enums.RequestPermissionsStatus
import com.rookmotion.rookconnectdemo.common.ConsoleOutput
import kotlinx.coroutines.launch

class PermissionsViewModel(
    private val rookPermissionsManager: RookPermissionsManager,
) : ViewModel() {

    val hcAvailabilityOutput = ConsoleOutput()
    val checkHCPermissionsOutput = ConsoleOutput()
    val checkHCPermissionsPartiallyOutput = ConsoleOutput()
    val requestHCPermissionsOutput = ConsoleOutput()
    val revokeHCPermissionsOutput = ConsoleOutput()
    val openHealthConnectOutput = ConsoleOutput()
    val checkAndroidPermissionsOutput = ConsoleOutput()
    val requestAndroidPermissionsOutput = ConsoleOutput()

    fun checkHealthConnectAvailability() {
        hcAvailabilityOutput.set("Verifying Health Connect availability...")

        viewModelScope.launch {
            val healthConnectAvailability = rookPermissionsManager.checkHealthConnectAvailability()

            val message = when (healthConnectAvailability) {
                HealthConnectAvailability.INSTALLED -> "Health Connect is installed"
                HealthConnectAvailability.NOT_INSTALLED -> "Health Connect is not installed"
                HealthConnectAvailability.NOT_SUPPORTED -> "This device is not compatible with health connect."
            }

            hcAvailabilityOutput.append(message)
        }
    }

    fun checkHealthConnectPermissions() {
        checkHCPermissionsOutput.set("Verifying Health Connect permissions...")

        viewModelScope.launch {
            rookPermissionsManager.checkHealthConnectPermissions().fold(
                {
                    if (it) {
                        checkHCPermissionsOutput.append("Health Connect permissions granted")
                    } else {
                        checkHCPermissionsOutput.append("There is one or more missing permissions")
                    }
                },
                {
                    checkHCPermissionsOutput.appendError(it, "Error verifying Health Connect permissions")
                }
            )
        }
    }

    fun checkHealthConnectPermissionsPartially() {
        checkHCPermissionsPartiallyOutput.set("Verifying Health Connect permissions...")

        viewModelScope.launch {
            rookPermissionsManager.checkHealthConnectPermissionsPartially().fold(
                {
                    if (it) {
                        checkHCPermissionsPartiallyOutput.append("Health Connect permissions partially granted")
                    } else {
                        checkHCPermissionsPartiallyOutput.append("No permission granted")
                    }
                },
                {
                    checkHCPermissionsPartiallyOutput.appendError(it, "Error verifying Health Connect permissions")
                },
            )
        }
    }

    fun requestHealthConnectPermissions() {
        requestHCPermissionsOutput.set("Requesting Health Connect permissions...")

        viewModelScope.launch {
            rookPermissionsManager.requestHealthConnectPermissions().fold(
                {
                    val message = when (it) {
                        RequestPermissionsStatus.ALREADY_GRANTED -> "Permissions already granted"
                        RequestPermissionsStatus.REQUEST_SENT -> "Permissions request sent, if nothing happens open Health Connect settings and give permissions manually"
                    }

                    requestHCPermissionsOutput.append(message)
                },
                {
                    requestHCPermissionsOutput.appendError(it, "Error requesting Health Connect permissions")
                },
            )
        }
    }

    fun onHealthConnectPermissionsResult(permissionsGranted: Boolean, permissionsPartiallyGranted: Boolean) {
        if (permissionsGranted) {
            checkHCPermissionsOutput.append("Permissions granted")
            requestHCPermissionsOutput.append("Permissions granted")
        } else {
            checkHCPermissionsOutput.append("There is one or more missing permissions")
            requestHCPermissionsOutput.append("There is one or more missing permissions")
        }

        if (permissionsPartiallyGranted) {
            checkHCPermissionsPartiallyOutput.append("Permissions partially granted")
        } else {
            checkHCPermissionsPartiallyOutput.append("No permission granted")
        }
    }

    fun revokeHealthConnectPermissions() {
        revokeHCPermissionsOutput.set("Revoking Health Connect permissions...")

        viewModelScope.launch {
            rookPermissionsManager.revokeHealthConnectPermissions().fold(
                {
                    revokeHCPermissionsOutput.append("Permissions revoked")
                },
                {
                    revokeHCPermissionsOutput.appendError(it, "Error revoking Health Connect permissions")
                }
            )
        }
    }

    fun openHealthConnectSettings() {
        openHealthConnectOutput.set("Opening Health Connect settings...")

        viewModelScope.launch {
            rookPermissionsManager.openHealthConnectSettings().fold(
                {
                    openHealthConnectOutput.append("Health Connect settings opened")
                },
                {
                    openHealthConnectOutput.appendError(it, "Error opening Health Connect settings")
                }
            )
        }
    }

    fun checkAndroidPermissions() {
        checkAndroidPermissionsOutput.set("Verifying Android permissions...")

        viewModelScope.launch {
            val permissionsGranted = rookPermissionsManager.checkAndroidPermissions()

            if (permissionsGranted) {
                checkAndroidPermissionsOutput.append("Android permissions granted")
            } else {
                checkAndroidPermissionsOutput.append("There is one or more missing permissions")
            }
        }
    }

    fun requestAndroidPermissions(activity: Activity) {
        requestAndroidPermissionsOutput.set("Verifying if permissions should be requested...")

        viewModelScope.launch {
            val shouldRequestPermissions = RookPermissionsManager.shouldRequestAndroidPermissions(activity)

            if (shouldRequestPermissions) {
                requestAndroidPermissionsOutput.set("Requesting Android permissions...")

                val requestPermissionsStatus = rookPermissionsManager.requestAndroidPermissions()

                val message = when (requestPermissionsStatus) {
                    RequestPermissionsStatus.ALREADY_GRANTED -> "Permissions already granted"
                    RequestPermissionsStatus.REQUEST_SENT -> "Permissions request sent, if nothing happens open application settings and give permissions manually"
                }

                requestAndroidPermissionsOutput.append(message)
            } else {
                requestAndroidPermissionsOutput.append("Android permissions previously denied, open app settings and give permissions manually")
            }
        }
    }

    fun onAndroidPermissionsResult(dialogDisplayed: Boolean, permissionsGranted: Boolean) {
        if (dialogDisplayed) {
            requestAndroidPermissionsOutput.append("Dialog was displayed")
        } else {
            requestAndroidPermissionsOutput.append("Android permissions previously denied, open app settings and give permissions manually")
        }

        if (permissionsGranted) {
            checkAndroidPermissionsOutput.append("Android permissions granted")
            requestAndroidPermissionsOutput.append("Android permissions granted")
        } else {
            checkAndroidPermissionsOutput.append("There is one or more missing permissions")
            requestAndroidPermissionsOutput.append("There is one or more missing permissions")
        }
    }
}

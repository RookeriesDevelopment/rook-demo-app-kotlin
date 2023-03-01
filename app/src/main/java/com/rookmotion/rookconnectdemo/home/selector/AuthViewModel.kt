package com.rookmotion.rookconnectdemo.home.selector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.auth.AuthorizationProvider
import com.rookmotion.rook.auth.domain.model.AuthorizationResult
import com.rookmotion.rook.users.RookUsersManager
import com.rookmotion.rook.users.domain.enums.UserType
import com.rookmotion.rookconnectdemo.BuildConfig
import com.rookmotion.rookconnectdemo.home.common.BasicState
import com.rookmotion.rookconnectdemo.home.common.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val provider: AuthorizationProvider,
    private val manager: RookUsersManager,
) : ViewModel() {

    private val _authorization = MutableStateFlow<DataState<AuthorizationResult>>(DataState.None)
    val authorization get() = _authorization.asStateFlow()

    private val _user = MutableStateFlow<BasicState>(BasicState.None)
    val user get() = _user.asStateFlow()

    init {
        getAuthorization(BuildConfig.CLIENT_UUID)
    }

    fun getAuthorization(clientUUID: String) {
        _authorization.tryEmit(DataState.Loading)

        viewModelScope.launch {
            try {
                val result = provider.getAuthorization(clientUUID)

                _authorization.emit(DataState.Success(result))
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) {
                    throw e
                } else {
                    _authorization.emit(DataState.Error(e.toString()))
                }
            }
        }
    }

    fun registerUser(clientUUID: String, userID: String) {
        _user.tryEmit(BasicState.Loading)

        viewModelScope.launch {
            try {
                manager.registerRookUser(clientUUID, userID, UserType.HEALTH_CONNECT)

                _user.emit(BasicState.Success)
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) {
                    throw e
                } else {
                    _user.emit(BasicState.Error(e.toString()))
                }
            }
        }
    }
}
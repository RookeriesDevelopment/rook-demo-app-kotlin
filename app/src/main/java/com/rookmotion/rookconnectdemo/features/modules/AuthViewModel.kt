package com.rookmotion.rookconnectdemo.features.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.auth.AuthorizationProvider
import com.rookmotion.rookconnectdemo.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authorizationProvider: AuthorizationProvider) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.NotAuthorized)
    val authState get() = _authState.asStateFlow()

    init {
        getAuthorization(BuildConfig.CLIENT_UUID)
    }

    fun getAuthorization(clientUUID: String) {
        viewModelScope.launch {
            _authState.emit(AuthState.Loading)

            try {
                val result = authorizationProvider.getAuthorization(clientUUID)

                _authState.emit(AuthState.Authorized(result))
            } catch (e: Exception) {
                _authState.emit(AuthState.Error(e.toString()))
            }
        }
    }
}

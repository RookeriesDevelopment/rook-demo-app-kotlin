package com.rookmotion.rookconnectdemo.features.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.users.RookUsersManager
import com.rookmotion.rook.users.domain.enums.UserType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val rookUsersManager: RookUsersManager) : ViewModel() {

    private val _userState = MutableStateFlow<UserState>(UserState.None)
    val userState get() = _userState.asStateFlow()

    fun registerUser(userID: String) {
        viewModelScope.launch {
            _userState.emit(UserState.Loading)

            try {
                rookUsersManager.registerRookUser(userID, UserType.HEALTH_CONNECT)

                _userState.emit(UserState.Registered)
            } catch (e: Exception) {
                _userState.emit(UserState.Error(e.toString()))
            }
        }
    }
}

package com.rookmotion.rookconnectdemo.features.connectionspage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.RookDataSources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConnectionsPageViewModel(
    private val rookDataSources: RookDataSources,
    private val connectionsPageUrl: String,
) : ViewModel() {

    private val _state = MutableStateFlow(ConnectionsPageState())
    val state get() = _state.asStateFlow()

    fun getAvailableDataSources() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }

            rookDataSources.getAvailableDataSources().fold(
                { dataSources ->
                    _state.update { it.copy(loading = false, dataSources = dataSources) }
                },
                { throwable ->
                    _state.update { it.copy(loading = false, error = "${throwable.message}") }
                },
            )
        }
    }

    fun openConnectionUrl(connectionUrl: String) {
        viewModelScope.launch {
            _state.update { it.copy(webViewUrl = connectionUrl) }
        }
    }

    fun closeConnectionUrl() {
        viewModelScope.launch {
            _state.update { it.copy(webViewUrl = null) }
        }
    }

    fun isHomePageUrl(url: String): Boolean {
        return url.startsWith(connectionsPageUrl)
    }
}
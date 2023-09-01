package com.rookmotion.rookconnectdemo.features.connectionspage.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rookconnectdemo.features.connectionspage.domain.repository.DataSourceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConnectionsPageViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val connectionsPageUrl: String,
    private val dataSourceRepository: DataSourceRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConnectionsPageState())
    val uiState get() = _uiState.asStateFlow()

    fun getDataSources(clientUUID: String, userID: String) {
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(loading = true) }

            val dataSources = dataSourceRepository.getDataSources(clientUUID, userID)

            _uiState.update { it.copy(loading = false, dataSources = dataSources) }
        }
    }

    fun openConnectionUrl(connectionUrl: String) {
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(webViewUrl = connectionUrl) }
        }
    }

    fun closeConnectionUrl() {
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(webViewUrl = null) }
        }
    }

    fun isHomePageUrl(url: String): Boolean {
        return url.startsWith(connectionsPageUrl)
    }
}

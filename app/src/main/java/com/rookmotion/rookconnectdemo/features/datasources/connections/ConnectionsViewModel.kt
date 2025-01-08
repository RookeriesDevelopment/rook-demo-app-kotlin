package com.rookmotion.rookconnectdemo.features.datasources.connections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.RookDataSources
import com.rookmotion.rook.sdk.domain.enums.DataSourceType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ConnectionsViewModel(
    private val rookDataSources: RookDataSources,
) : ViewModel() {

    private val _events = Channel<ConnectionsEvents>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow<ConnectionsState>(ConnectionsState.Loading)
    val state get() = _state.asStateFlow()

    init {
        getAvailableDataSources()
    }

    fun getAvailableDataSources() {
        Timber.i("Getting available data sources")

        viewModelScope.launch {
            _state.emit(ConnectionsState.Loading)

            rookDataSources.getAvailableDataSources(redirectUrl = REDIRECT_URL).fold(
                {
                    _state.emit(ConnectionsState.Success(it))
                    Timber.i("Success getting available data sources")
                },
                {
                    _state.emit(ConnectionsState.Error(it.message ?: "Unknown error"))
                    Timber.e(it, "Error getting available data sources")
                },
            )
        }
    }

    fun disconnectFromDataSource(dataSourceType: DataSourceType?) {
        viewModelScope.launch {
            if (dataSourceType == null) {
                _events.send(ConnectionsEvents.DisconnectionNotSupported)
            } else {
                rookDataSources.revokeDataSource(dataSourceType).fold(
                    {
                        _events.send(ConnectionsEvents.DisconnectionSuccess(dataSourceType))
                    },
                    {
                        _events.send(ConnectionsEvents.DisconnectionFailed(dataSourceType))
                        getAvailableDataSources()
                    },
                )
            }
        }
    }
}

// Replace with Android App Link or page with instructions to close the CustomTabs view
private const val REDIRECT_URL = "https://www.google.com"

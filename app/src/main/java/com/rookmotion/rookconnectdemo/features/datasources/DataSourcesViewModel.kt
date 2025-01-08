package com.rookmotion.rookconnectdemo.features.datasources

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.RookDataSources
import com.rookmotion.rookconnectdemo.common.ConsoleOutput
import kotlinx.coroutines.launch
import timber.log.Timber

class DataSourcesViewModel(
    private val rookDataSources: RookDataSources,
) : ViewModel() {

    fun presentDataSourceView() {
        Timber.i("Presenting data source view")

        rookDataSources.presentDataSourceView(REDIRECT_URL)
    }
}

private const val REDIRECT_URL = "https://www.google.com"

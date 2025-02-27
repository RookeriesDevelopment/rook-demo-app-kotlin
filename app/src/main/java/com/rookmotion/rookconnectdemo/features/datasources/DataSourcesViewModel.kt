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

    val authorizedDataSourcesOutput = ConsoleOutput()

    fun getAuthorizedDataSources() {
        authorizedDataSourcesOutput.set("Getting authorized data sources")

        viewModelScope.launch {
            rookDataSources.getAuthorizedDataSources().fold(
                {
                    authorizedDataSourcesOutput.appendMultiple(
                        "Success getting authorized data sources",
                        "Oura: ${it.oura}",
                        "Polar: ${it.polar}",
                        "Whoop: ${it.whoop}",
                        "Fitbit: ${it.fitbit}",
                        "Garmin: ${it.garmin}",
                        "Withings: ${it.withings}",
                        "Apple Health: ${it.appleHealth}",
                        "Health Connect: ${it.healthConnect}",
                        "Android: ${it.android}",
                    )
                },
                {
                    authorizedDataSourcesOutput.appendError(it, "Error getting authorized data sources")
                },
            )
        }
    }

    fun presentDataSourceView() {
        Timber.i("Presenting data source view")

        rookDataSources.presentDataSourceView(REDIRECT_URL)
    }
}

// Replace with Android App Link or page with instructions to close the CustomTabs view
private const val REDIRECT_URL = "https://www.google.com"

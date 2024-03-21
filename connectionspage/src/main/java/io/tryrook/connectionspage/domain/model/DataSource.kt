package io.tryrook.connectionspage.domain.model

data class DataSource(
    val name: String,
    val description: String,
    val thumbnail: String,
    val connected: Boolean,
    val connectionUrl: String?,
) {

    /**
     * Safer connected check.
     */
    val isConnected get() = connected || connectionUrl == null
}

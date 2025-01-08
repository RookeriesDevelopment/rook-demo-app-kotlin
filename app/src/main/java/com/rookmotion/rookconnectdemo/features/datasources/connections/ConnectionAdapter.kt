package com.rookmotion.rookconnectdemo.features.datasources.connections

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rookmotion.rook.sdk.domain.enums.DataSourceType
import com.rookmotion.rook.sdk.domain.model.DataSource
import com.rookmotion.rookconnectdemo.R
import com.rookmotion.rookconnectdemo.databinding.ListTileConnectionBinding

class ConnectionAdapter(
    private val onConnectClick: (String) -> Unit,
    private val onDisconnectClick: (DataSourceType?) -> Unit,
) : ListAdapter<DataSource, ConnectionAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ListTileConnectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dataSource: DataSource) {
            binding.thumbnail.load(dataSource.imageUrl)
            binding.title.text = dataSource.name
            binding.subtitle.text = dataSource.description

            val authorizationUrl = dataSource.authorizationUrl

            if (authorizationUrl == null) {
                binding.disconnect.setOnClickListener {
                    onDisconnectClick(getDataSourceType(dataSource))
                }

                binding.connect.isEnabled = false
                binding.disconnect.isEnabled = true
            } else {
                binding.connect.setOnClickListener {
                    onConnectClick(authorizationUrl)
                }

                binding.connect.isEnabled = true
                binding.disconnect.isEnabled = false
            }

            if (dataSource.connected) {
                binding.connect.setText(R.string.connected)
                binding.connect.isEnabled = false
            } else {

                binding.connect.setText(R.string.connect)
                binding.connect.isEnabled = true
            }
        }

        private fun getDataSourceType(dataSource: DataSource): DataSourceType? {
            return when (dataSource.name) {
                "Garmin" -> DataSourceType.GARMIN
                "Oura" -> DataSourceType.OURA
                "Polar" -> DataSourceType.POLAR
                "Fitbit" -> DataSourceType.FITBIT
                "Withings" -> DataSourceType.WITHINGS
                "Whoop" -> DataSourceType.WHOOP
                else -> null
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<DataSource>() {
        override fun areItemsTheSame(oldItem: DataSource, newItem: DataSource): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: DataSource, newItem: DataSource): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListTileConnectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

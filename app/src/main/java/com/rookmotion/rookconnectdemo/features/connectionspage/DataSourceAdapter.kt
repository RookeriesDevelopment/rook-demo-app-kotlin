package com.rookmotion.rookconnectdemo.features.connectionspage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rookmotion.rookconnectdemo.R
import com.rookmotion.rookconnectdemo.databinding.ListTileDataSourceBinding

class DataSourceAdapter(
    private val onClick: (String) -> Unit,
) : ListAdapter<com.rookmotion.rook.sdk.domain.model.DataSource, DataSourceAdapter.ViewHolder>(
    DiffCallback()
) {

    inner class ViewHolder(private val binding: ListTileDataSourceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dataSource: com.rookmotion.rook.sdk.domain.model.DataSource) {
            binding.thumbnail.load(dataSource.image)
            binding.title.text = dataSource.name
            binding.subtitle.text = dataSource.description

            if (dataSource.connected) {
                binding.connect.setText(R.string.connected)
                binding.connect.isEnabled = false
            } else {
                binding.connect.setOnClickListener {
                    if (dataSource.authorizationUrl != null) {
                        onClick(dataSource.authorizationUrl!!)
                    } else {
                        // Should never happen because we are checking if connected is true...
                    }
                }
                binding.connect.setText(R.string.connect)
                binding.connect.isEnabled = true
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<com.rookmotion.rook.sdk.domain.model.DataSource>() {
        override fun areItemsTheSame(
            oldItem: com.rookmotion.rook.sdk.domain.model.DataSource,
            newItem: com.rookmotion.rook.sdk.domain.model.DataSource,
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: com.rookmotion.rook.sdk.domain.model.DataSource,
            newItem: com.rookmotion.rook.sdk.domain.model.DataSource,
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListTileDataSourceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

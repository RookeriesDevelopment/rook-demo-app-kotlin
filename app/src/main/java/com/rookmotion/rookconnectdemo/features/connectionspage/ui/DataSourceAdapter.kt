package com.rookmotion.rookconnectdemo.features.connectionspage.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rookmotion.rookconnectdemo.R
import com.rookmotion.rookconnectdemo.databinding.ListTileDataSourceBinding
import com.rookmotion.rookconnectdemo.features.connectionspage.domain.model.DataSource

class DataSourceAdapter(
    private val onClick: (String) -> Unit,
) : ListAdapter<DataSource, DataSourceAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ListTileDataSourceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dataSource: DataSource) {
            binding.thumbnail.load(dataSource.thumbnail)
            binding.title.text = dataSource.name
            binding.subtitle.text = dataSource.description

            if (dataSource.isConnected) {
                binding.connect.setText(R.string.connected)
                binding.connect.isEnabled = false
            } else {
                binding.connect.setOnClickListener { onClick(dataSource.connectionUrl!!) }
                binding.connect.setText(R.string.connect)
                binding.connect.isEnabled = true
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
            ListTileDataSourceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
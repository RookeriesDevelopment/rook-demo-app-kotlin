package com.rookmotion.rookconnectdemo.features.datasources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rookmotion.rookconnectdemo.databinding.FragmentDataSourcesBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.displayConsoleOutputUpdates
import com.rookmotion.rookconnectdemo.extension.serviceLocator
import com.rookmotion.rookconnectdemo.features.datasources.connections.ConnectionsBottomSheet

class DataSourcesFragment : Fragment() {

    private var _binding: FragmentDataSourcesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DataSourcesViewModel> { ViewModelFactory(serviceLocator) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataSourcesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayConsoleOutputUpdates(
            consoleOutput = viewModel.authorizedDataSourcesOutput,
            receiver = binding.getAuthorizedDataSourcesOutput,
        )

        binding.getAuthorizedDataSources.setOnClickListener {
            viewModel.getAuthorizedDataSources()
        }

        binding.connectionsPageDataSourcesList.setOnClickListener {
            ConnectionsBottomSheet().show(
                requireActivity().supportFragmentManager,
                ConnectionsBottomSheet.TAG
            )
        }

        binding.presentDataSourceView.setOnClickListener {
            viewModel.presentDataSourceView()
        }
    }
}

package com.rookmotion.rookconnectdemo.features.connectionspage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rookmotion.rookconnectdemo.BuildConfig
import com.rookmotion.rookconnectdemo.R
import com.rookmotion.rookconnectdemo.common.USER_ID
import com.rookmotion.rookconnectdemo.databinding.FragmentConnectionsPageBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.enableJavaScriptAndDomStorage
import com.rookmotion.rookconnectdemo.extension.overrideOnBackPressed
import com.rookmotion.rookconnectdemo.extension.repeatOnResume
import com.rookmotion.rookconnectdemo.extension.serviceLocator
import com.rookmotion.rookconnectdemo.extension.setOnNavigationRequestListener
import kotlinx.coroutines.flow.collectLatest

class ConnectionsPageFragment : Fragment() {

    private var _binding: FragmentConnectionsPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var dataSourceAdapter: DataSourceAdapter

    private val connectionsPageViewModel by viewModels<ConnectionsPageViewModel> {
        ViewModelFactory(serviceLocator)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentConnectionsPageBinding.inflate(inflater, container, false)
        dataSourceAdapter = DataSourceAdapter { connectionsPageViewModel.openConnectionUrl(it) }

        overrideOnBackPressed {
            if (binding.webView.isVisible) {
                if (binding.webView.canGoBack()) {
                    binding.webView.goBack()
                } else {
                    connectionsPageViewModel.closeConnectionUrl()
                }
            } else {
                findNavController().navigateUp()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observers()
        bindings()

        connectionsPageViewModel.getDataSources(BuildConfig.CLIENT_UUID, USER_ID)
    }

    private fun observers() {
        repeatOnResume {
            connectionsPageViewModel.uiState.collectLatest { state ->
                if (state.loading) {
                    showProgress()
                } else {
                    if (state.webViewUrl == null) {
                        binding.webView.loadUrl(BLANK_PAGE)

                        if (state.dataSources.isEmpty()) {
                            showError()
                        } else {
                            showItems()

                            dataSourceAdapter.submitList(state.dataSources)
                        }
                    } else {
                        showWebView(state.webViewUrl)
                    }
                }
            }
        }
    }

    private fun bindings() {
        binding.error.retry.setOnClickListener {
            connectionsPageViewModel.getDataSources(BuildConfig.CLIENT_UUID, USER_ID)
        }
        binding.items.setHasFixedSize(true)
        binding.items.adapter = dataSourceAdapter
    }

    private fun showProgress() {
        binding.progress.root.isVisible = true
        binding.error.root.isVisible = false
        binding.items.isVisible = false
        binding.webView.isVisible = false
    }

    private fun showError() {
        binding.error.message.setText(R.string.no_data_sources_found)

        binding.progress.root.isVisible = false
        binding.error.root.isVisible = true
        binding.items.isVisible = false
        binding.webView.isVisible = false
    }

    private fun showItems() {
        binding.progress.root.isVisible = false
        binding.error.root.isVisible = false
        binding.items.isVisible = true
        binding.webView.isVisible = false
    }

    private fun showWebView(url: String) {
        binding.webView.enableJavaScriptAndDomStorage()
        binding.webView.setOnNavigationRequestListener { request ->
            connectionsPageViewModel.isHomePageUrl(request.url.toString()).also {
                if (it) {
                    connectionsPageViewModel.closeConnectionUrl()
                    connectionsPageViewModel.getDataSources(
                        BuildConfig.CLIENT_UUID,
                        USER_ID,
                    )
                }
            }
        }

        binding.webView.loadUrl(url)

        binding.progress.root.isVisible = false
        binding.error.root.isVisible = false
        binding.items.isVisible = false
        binding.webView.isVisible = true
    }
}

private const val BLANK_PAGE = "about:blank"

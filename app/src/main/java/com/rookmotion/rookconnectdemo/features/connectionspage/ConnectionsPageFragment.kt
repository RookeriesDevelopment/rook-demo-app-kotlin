package com.rookmotion.rookconnectdemo.features.connectionspage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rookmotion.rookconnectdemo.databinding.FragmentConnectionsPageBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.enableJavaScriptAndDomStorage
import com.rookmotion.rookconnectdemo.extension.overrideOnBackPressed
import com.rookmotion.rookconnectdemo.extension.repeatOnResume
import com.rookmotion.rookconnectdemo.extension.serviceLocator
import com.rookmotion.rookconnectdemo.extension.setOnNavigationRequestListener
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

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

        connectionsPageViewModel.getAvailableDataSources()
    }

    private fun observers() {
        repeatOnResume {
            connectionsPageViewModel.state.collectLatest { state ->
                if (state.loading) {
                    showProgress()
                } else {
                    if (state.webViewUrl == null) {
                        binding.webView.loadUrl(BLANK_PAGE)

                        if (state.error != null) {
                            showError(state.error)
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
            connectionsPageViewModel.getAvailableDataSources()
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

    private fun showError(error: String) {
        binding.error.message.text = error

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
            Timber.i("Navigation requested to: ${request.url}")

            connectionsPageViewModel.isHomePageUrl(request.url.toString()).also {
                if (it) {
                    connectionsPageViewModel.closeConnectionUrl()
                    connectionsPageViewModel.getAvailableDataSources()
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

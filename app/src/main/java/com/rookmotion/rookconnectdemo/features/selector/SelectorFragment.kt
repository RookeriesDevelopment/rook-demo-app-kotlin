package com.rookmotion.rookconnectdemo.features.selector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rookmotion.rookconnectdemo.databinding.FragmentSelectorBinding
import com.rookmotion.rookconnectdemo.extension.setNavigateOnClick

class SelectorFragment : Fragment() {

    private var _binding: FragmentSelectorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSelectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sdk.setNavigateOnClick(
            SelectorFragmentDirections.actionSelectorFragmentToSDKFragment()
        )
        binding.modules.setNavigateOnClick(
            SelectorFragmentDirections.actionSelectorFragmentToModulesFragment()
        )
        binding.connectionsPage.setNavigateOnClick(
            SelectorFragmentDirections.actionSelectorFragmentToConnectionsPageFragment()
        )
    }
}
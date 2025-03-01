package com.rookmotion.rookconnectdemo.extension

import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rookmotion.rookconnectdemo.RookConnectDemoApplication
import com.rookmotion.rookconnectdemo.common.ConsoleOutput
import com.rookmotion.rookconnectdemo.di.ServiceLocator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

val Fragment.serviceLocator: ServiceLocator get() = (requireActivity().application as RookConnectDemoApplication).serviceLocator

fun Fragment.repeatOnResume(block: suspend () -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            block()
        }
    }
}

fun Fragment.overrideOnBackPressed(block: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(
        viewLifecycleOwner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                block()
            }
        },
    )
}

fun Fragment.displayConsoleOutputUpdates(consoleOutput: ConsoleOutput, receiver: TextView) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            consoleOutput.output.collectLatest {
                receiver.text = it
            }
        }
    }
}

package com.rookmotion.rookconnectdemo.utils

import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar

fun View.snackShort(message: String) {
    Snackbar.make(
        this, message, Snackbar.LENGTH_SHORT
    ).show()
}

fun View.snackLong(message: String, action: String, onClick: () -> Unit) {
    Snackbar.make(
        this, message, Snackbar.LENGTH_LONG
    ).setAction(action) { onClick() }.show()
}

fun MaterialButton.setNavigateOnClick(directions: NavDirections) {
    setOnClickListener(Navigation.createNavigateOnClickListener(directions))
}
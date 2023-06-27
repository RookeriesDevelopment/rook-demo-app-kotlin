package com.rookmotion.rookconnectdemo.extension

import android.view.View
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

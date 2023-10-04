package com.rookmotion.rookconnectdemo.extension

import android.widget.TextView
import androidx.annotation.DrawableRes

fun TextView.clearCompoundDrawablesWithIntrinsicBounds() {
    setCompoundDrawablesWithIntrinsicBounds(
        0, 0, 0, 0
    )
}

fun TextView.setStartCompoundDrawableWithIntrinsicBounds(@DrawableRes id: Int) {
    setCompoundDrawablesWithIntrinsicBounds(
        id, 0, 0, 0
    )
}

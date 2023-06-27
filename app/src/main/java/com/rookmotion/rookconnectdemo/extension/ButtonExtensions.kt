package com.rookmotion.rookconnectdemo.extension

import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton

fun MaterialButton.setNavigateOnClick(directions: NavDirections) {
    setOnClickListener(Navigation.createNavigateOnClickListener(directions))
}
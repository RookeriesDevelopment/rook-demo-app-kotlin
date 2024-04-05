package com.rookmotion.rookconnectdemo.common

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openPlayStore(context: Context) {
    context.startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata")
        )
    )
}

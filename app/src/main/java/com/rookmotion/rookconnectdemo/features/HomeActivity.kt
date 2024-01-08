package com.rookmotion.rookconnectdemo.features

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.rookmotion.rook.sdk.domain.delegate.rookYesterdaySync
import com.rookmotion.rook.sdk.domain.environment.RookEnvironment
import com.rookmotion.rookconnectdemo.BuildConfig
import com.rookmotion.rookconnectdemo.R
import com.rookmotion.rookconnectdemo.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val rookYesterdaySync by rookYesterdaySync(
        enableLogs = BuildConfig.DEBUG,
        ignoreConfigChange = true,
        clientUUID = BuildConfig.CLIENT_UUID,
        secretKey = BuildConfig.SECRET_KEY,
        environment = if (BuildConfig.DEBUG) RookEnvironment.SANDBOX else RookEnvironment.PRODUCTION,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        // Must be before super.onCreate(savedInstanceState)
        // Before calling `rookYesterdaySync.enable` verify that your app has all permissions granted,
        // calling `rookYesterdaySync.enable` without permissions WON'T crash the app but all sync processes will fail until the permissions are granted,
        // so, it's safe to call `rookYesterdaySync.enable` and then request permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            rookYesterdaySync.enable(this)
        }

        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val host = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val controller = host.navController

        appBarConfiguration = AppBarConfiguration(controller.graph)

        setupActionBarWithNavController(controller, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
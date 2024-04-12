package com.rookmotion.rookconnectdemo.features

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.rookmotion.rook.sdk.domain.enums.SyncInstruction
import com.rookmotion.rook.sdk.framework.delegate.rookYesterdaySync
import com.rookmotion.rookconnectdemo.BuildConfig
import com.rookmotion.rookconnectdemo.HomeViewModel
import com.rookmotion.rookconnectdemo.R
import com.rookmotion.rookconnectdemo.common.isDebug
import com.rookmotion.rookconnectdemo.common.rookEnvironment
import com.rookmotion.rookconnectdemo.databinding.ActivityHomeBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.serviceLocator

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory(serviceLocator)
    }

    private val rookYesterdaySync by rookYesterdaySync(
        enableLogs = isDebug,
        clientUUID = BuildConfig.CLIENT_UUID,
        secretKey = BuildConfig.SECRET_KEY,
        environment = rookEnvironment,
        state = Lifecycle.State.CREATED,
        doOnEnd = SyncInstruction.NOTHING,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        // Must be before super.onCreate(savedInstanceState)
        // Using userAcceptedYesterdaySync we ensure to enable the YesterdaySync feature only if the user accepted it
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && homeViewModel.userAcceptedYesterdaySync) {
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
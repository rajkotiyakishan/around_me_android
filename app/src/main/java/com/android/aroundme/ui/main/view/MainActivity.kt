package com.android.aroundme.ui.main.view


import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.android.aroundme.CoreActivity
import com.android.aroundme.R
import com.android.aroundme.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class MainActivity : CoreActivity<ActivityMainBinding>() {

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun createReference() {
        setupUI()
    }

    private fun setupUI() {
        val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        val appBarConfiguration: AppBarConfiguration = AppBarConfiguration(navController.graph)
        getBinding().toolbar.setupWithNavController(navController, appBarConfiguration)

        /*  getBinding().toolbar.setNavigationOnClickListener {
              // Handle the back button event and return to override
              // the default behavior the same way as the OnBackPressedCallback.
              // TODO(reason: handle custom back behavior here if desired.)

              // If no custom behavior was handled perform the default action.
              navController.navigateUp(appBarConfiguration)
          }*/


    }

}

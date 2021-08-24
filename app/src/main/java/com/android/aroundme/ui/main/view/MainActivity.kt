package com.android.aroundme.ui.main.view


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.android.aroundme.CoreActivity
import com.android.aroundme.R
import com.android.aroundme.databinding.ActivityMainBinding
import com.android.aroundme.utils.ktx.simpleAlert
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class MainActivity : CoreActivity<ActivityMainBinding>() {

    private val RESOLUTION_CODE = 11
    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun createReference() {
        setupUI()
        displayLocationSettingsRequest(this)
    }

    private fun setupUI() {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        val appBarConfiguration: AppBarConfiguration = AppBarConfiguration(navController.graph)
        getBinding().toolbar.setupWithNavController(navController, appBarConfiguration)

    }

    private fun displayLocationSettingsRequest(context: Context) {
        val googleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API).build()
        googleApiClient.connect()
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = (10000 / 2).toLong()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result: PendingResult<LocationSettingsResult> =
            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { result ->
            val status: Status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    setupUI()
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    // Location settings are not satisfied. Show the user a dialog to upgrade location settings
                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        status.startResolutionForResult(
                            this,RESOLUTION_CODE)
                    } catch (e: IntentSender.SendIntentException) {
                        // PendingIntent unable to execute request
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    // Location settings are inadequate, and cannot be fixed here. Dialog not created
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESOLUTION_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                   //   setupUI()
                }
                Activity.RESULT_CANCELED -> {
                    // User denied to access location asking again with info
                    showPermissionDialog()
                }
            }
        }
    }

    private fun showPermissionDialog() {
        simpleAlert(
            title = getString(R.string.app_name),
            msg = getString(R.string.read_location),
            btnTitle = getString(R.string.settings),
            positiveButton = {
                displayLocationSettingsRequest(this)
            }
        )
    }


}

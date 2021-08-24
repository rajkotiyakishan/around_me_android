package com.android.aroundme.ui.main.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.android.aroundme.CoreApp
import com.android.aroundme.CoreFragment
import com.android.aroundme.R
import com.android.aroundme.databinding.FragmentMainBinding
import com.android.aroundme.ui.main.viewmodel.MainViewModel
import com.android.aroundme.utils.ktx.PermissionCallBack
import com.android.aroundme.utils.ktx.confirmationDialog
import com.android.aroundme.utils.ktx.setLat
import com.android.aroundme.utils.ktx.setLong
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import org.supportcompact.adapters.Page
import org.supportcompact.adapters.setFragmentPagerAdapter


@AndroidEntryPoint
class MainFragment : CoreFragment<FragmentMainBinding>() {
    private val mainViewModel: MainViewModel by viewModels()
    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 30
        fastestInterval = 10
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        maxWaitTime = 60
        numUpdates = 1
    }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                val location = locationList.last()
                CoreApp.mInstance?.setLat(location.latitude.toString())
                CoreApp.mInstance?.setLong(location.longitude.toString())
                getBinding().progressBar.visibility = View.GONE
                setUpTabLayout(location)
            }
        }
    }


    override fun getLayout(): Int {
        return R.layout.fragment_main
    }

    override fun setVM(binding: FragmentMainBinding) {
        binding.vm = mainViewModel
        binding.executePendingBindings()
    }

    override fun createReference() {
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireContext())
        getCurrentLocation()

    }

    private fun setUpTabLayout(location: Location) {

        // add pages in viewpager
        val pages = arrayListOf(
            Page("", PlaceListFragment.newInstance()),
            Page("", PlaceMapFragment.newInstance())
        )

        getBinding().mPager.isNestedScrollingEnabled = false
        getBinding().mPager.offscreenPageLimit = 1
        getBinding().mPager.setFragmentPagerAdapter(childFragmentManager, pages)

        // set viewpager in tabLayout
        getBinding().mTab.setupWithViewPager(getBinding().mPager, true)

        getBinding().mTab.getTabAt(0)?.setIcon(R.drawable.ic_list)
        getBinding().mTab.getTabAt(1)?.setIcon(R.drawable.ic_map)


        // calling api to get place
        mainViewModel.fetchPlaces(
            (location.latitude.toString().plus(",").plus(location.longitude.toString()))
        )

    }

    private fun getCurrentLocation() {
        val permissions = arrayListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        requestPermissionsIfRequired(permissions, object : PermissionCallBack {
            override fun permissionGranted() {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    getCurrentLocation()
                } else {
                    getBinding().progressBar.visibility = View.VISIBLE
                    // start location update request
                    fusedLocationProvider?.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                }
            }

            override fun permissionDenied() {
                getCurrentLocation()
            }

            override fun onPermissionDisabled() {
                requireActivity().confirmationDialog(
                    title = getString(R.string.app_name),
                    msg = getString(R.string.read_location),
                    btnPositive = getString(R.string.settings),
                    btnNegative = getString(R.string.cancel),
                    btnPositiveClick = {
                        val intent = Intent()
                        intent.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
                        val uri = Uri.fromParts("package", context?.packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                )
            }
        })
    }


}
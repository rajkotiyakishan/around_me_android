package com.android.aroundme.ui.main.view

import android.Manifest
import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.aroundme.CoreFragment
import com.android.aroundme.R
import com.android.aroundme.data.model.Places
import com.android.aroundme.databinding.FragmentPlaceMapBinding
import com.android.aroundme.ui.main.viewmodel.MainViewModel
import com.android.aroundme.utils.Status
import com.android.aroundme.utils.ktx.positionCamera
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PlaceMapFragment : CoreFragment<FragmentPlaceMapBinding>() {
    private var mainViewModel: MainViewModel? = null
    private var mMap: GoogleMap? = null

    override fun getLayout(): Int {
        return R.layout.fragment_place_map
    }

    override fun setVM(binding: FragmentPlaceMapBinding) {
        binding.vm = mainViewModel
        binding.executePendingBindings()
    }

    override fun createReference() {
        parentFragment?.let {
            mainViewModel =
                ViewModelProvider(it).get(MainViewModel::class.java)
        }
        setupObserver()
    }

    private fun setMap(placeList: ArrayList<Places>) {
        val map = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        map.getMapAsync { googleMap ->
            mMap = googleMap
            setCurrentPlace(placeList[0])
            placeList.forEach { place ->
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(place.geometry.location.lat, place.geometry.location.lng))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                        .title(place.name)
                )
            }
            enableMyLocationIfPermitted()
            googleMap.positionCamera(
                LatLng(
                    placeList[0].geometry.location.lat,
                    placeList[0].geometry.location.lng
                )
            )

            googleMap.setOnMarkerClickListener(OnMarkerClickListener { marker ->
                if (marker.isInfoWindowShown) {
                    marker.hideInfoWindow()
                } else {
                    marker.showInfoWindow()
                }
                val place = placeList.find {
                    it.geometry.location.lat == marker.position.latitude &&
                            it.geometry.location.lng == marker.position.longitude
                }
                place?.let {
                    setCurrentPlace(it)
                }
                true
            })

        }

    }

    private fun enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        )
            mMap?.isMyLocationEnabled = true
    }

    private fun setupObserver() {
        mainViewModel?.places?.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { list ->
                        setMap(list as ArrayList<Places>)
                    }

                }
                Status.LOADING -> {


                }
                Status.ERROR -> {

                }
            }
        })
    }

    private fun setCurrentPlace(place: Places) {
        getBinding().tvPlaceName.text = place.name
        getBinding().tvAddress.text = place.vicinity
        getBinding().ratingBar.rating = place.ratings
        getBinding().tvDistance.text = place.geometry.location.getDistance
        place.openingHours?.let {
            getBinding().tvStatus.text =
                if (it.isOpenNow) requireContext().getText(R.string.open_now) else requireContext().getText(
                    R.string.closed_now
                )

            getBinding().tvStatus.setTextColor(
                if (it.isOpenNow) ContextCompat.getColor(
                    requireContext(),
                    R.color.purple_500
                ) else ContextCompat.getColor(requireContext(), R.color.red)
            )
        }


        place.photos?.get(0)?.placeImage?.let {
            Glide.with(requireContext()).load(it).circleCrop().into(getBinding().ivPlace)
        }

        getBinding().ivDirection.setOnClickListener {
            findNavController().navigate(
                R.id.placeDirectionFragment,
                bundleOf("place" to place)
            )

        }

    }


    companion object {
        @JvmStatic
        fun newInstance() =
            PlaceMapFragment().apply {

            }
    }
}
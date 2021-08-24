package com.android.aroundme.ui.placeDirection.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.android.aroundme.CoreApp
import com.android.aroundme.CoreFragment
import com.android.aroundme.R
import com.android.aroundme.data.model.GoogleMapDirection
import com.android.aroundme.data.model.Places
import com.android.aroundme.databinding.FragmentPlaceDirectionBinding
import com.android.aroundme.ui.main.view.MainActivity
import com.android.aroundme.ui.main.viewmodel.MainViewModel
import com.android.aroundme.ui.placeDirection.viewmodel.PlaceDirectionVM
import com.android.aroundme.utils.Status
import com.android.aroundme.utils.ktx.*
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceDirectionFragment : CoreFragment<FragmentPlaceDirectionBinding>() {
    private val placeDirectionVM: PlaceDirectionVM by viewModels()
    var place: Places? = null
    private var mMap: GoogleMap? = null
    private var polyLine: Polyline? = null
    private var lineOptions: PolylineOptions? = null
    private var listOfPoints = java.util.ArrayList<LatLng>()
    private var arrayRoutes = java.util.ArrayList<java.util.ArrayList<HashMap<String, String>>>()

    override fun getLayout(): Int {
        return R.layout.fragment_place_direction
    }

    override fun setVM(binding: FragmentPlaceDirectionBinding) {
        binding.vm = placeDirectionVM
        binding.executePendingBindings()
    }

    override fun createReference() {
        place = arguments?.getParcelable<Places>("place")
        place?.let {
            (requireActivity() as MainActivity).getBinding().toolbar.title = place?.name
            setCurrentPlace(it)
            setupObserver()
            val map = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
            map.getMapAsync { googleMap ->
                mMap = googleMap
                enableMyLocationIfPermitted()
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(it.geometry.location.lat, it.geometry.location.lng))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                        .title(it.name)
                )?.showInfoWindow()

                placeDirectionVM.fetchRoute(
                    CoreApp.mInstance?.getLat()?.toDouble(),
                    CoreApp.mInstance?.getLong()?.toDouble(),
                    it.geometry.location.lat,
                    it.geometry.location.lng
                )

            }
        }

    }


    private fun setupObserver() {
        placeDirectionVM.route.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    getBinding().progressBar.visibility = View.GONE
                    it.data?.let { direction ->
                        drawRout(direction)
                    }

                }
                Status.LOADING -> {
                    getBinding().progressBar.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    getBinding().progressBar.visibility = View.GONE
                    getBinding().mainCl.snackBar(it.message!!)
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

    private fun drawRout(directionArray: GoogleMapDirection) {
        if (polyLine != null)
            polyLine?.remove()

        if (!directionArray.arrayListRouts.isNullOrEmpty()) {
            for (rout in directionArray.arrayListRouts) {
                val path = java.util.ArrayList<HashMap<String, String>>()
                for (legs in rout.arrayListLegs) {
                    for (steps in legs.arrayListSteps) {

                        listOfPoints = (decodePoly(steps.polyline.points!!))

                        for (latlng in listOfPoints) {
                            val hm = HashMap<String, String>()
                            hm["lat"] = latlng.latitude.toString()
                            hm["lng"] = latlng.longitude.toString()
                            path.add(hm)
                        }
                    }
                    arrayRoutes.add(path)
                }
            }
        }
        addPath()
    }

    private fun addPath() {
        for (routs in arrayRoutes) {
            lineOptions = PolylineOptions()
            for (path in routs) {
                val lat = path["lat"]?.toDouble()
                val lng = path["lng"]?.toDouble()
                val position = LatLng(lat!!, lng!!)
                lineOptions?.add(position)
            }
        }
        lineOptions?.width(15f)
        lineOptions?.color(ContextCompat.getColor(requireContext(), R.color.purple_700))

        // Drawing polyline in the Google Map for the i-th route
        if (lineOptions != null) {
            polyLine = mMap?.addPolyline(lineOptions)
            val bounds: LatLngBounds = LatLngBounds.Builder().apply {
                lineOptions!!.points.forEach { point -> include(point) }
            }.build()

            mMap?.positionCamera(
                bounds,
                (childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment).view?.width?.div(
                    5
                ) ?: 5,
                true
            )

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PlaceDirectionFragment().apply {

            }
    }

}
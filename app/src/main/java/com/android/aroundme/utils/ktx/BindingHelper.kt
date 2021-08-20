package com.android.aroundme.utils.ktx

import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds


@BindingAdapter("imgUrl")
fun setIndicator(img: AppCompatImageView, url: String?) {
    url?.let {
        Glide.with(img).load(it).circleCrop().into(img)
    }
}


fun GoogleMap.positionCamera(location: LatLng, zoom: Float = 12f, animate: Boolean = true) {
    val cameraPosition = CameraPosition.fromLatLngZoom(location, zoom)
    val update = CameraUpdateFactory.newCameraPosition(cameraPosition)
    moveCamera(update, animate)
}

fun GoogleMap.positionCamera(bounds: LatLngBounds, padding: Int, animate: Boolean = true) {
    val update = CameraUpdateFactory.newLatLngBounds(bounds, padding)
    moveCamera(update, animate)
}

private fun GoogleMap.moveCamera(cameraUpdate: CameraUpdate, animate: Boolean = true) {
    if (animate) {
        this.animateCamera(cameraUpdate)
    } else {
        this.moveCamera(cameraUpdate)
    }
}

 fun decodePoly(encoded: String): java.util.ArrayList<LatLng> {
    val poly = java.util.ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].toInt() - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].toInt() - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val p = LatLng(
            lat.toDouble() / 1E5,
            lng.toDouble() / 1E5
        )
        poly.add(p)
    }

    return poly
}


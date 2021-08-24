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



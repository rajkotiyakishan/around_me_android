package com.android.aroundme.utils.ktx

import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


@BindingAdapter("imgUrl")
fun setIndicator(img: AppCompatImageView, url: String?) {
    url?.let {
        Glide.with(img).load(it).circleCrop().into(img)
    }
}
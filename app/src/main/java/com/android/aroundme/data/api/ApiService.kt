package com.android.aroundme.data.api

import com.android.aroundme.BuildConfig
import com.android.aroundme.data.model.PlaceData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("place/nearbysearch/json")
    suspend fun getNearbyPlace(
        @Query("location") location: String,
        @Query("radius") radius: String? = "10000",
        @Query("type") type: String? = "restaurant",
        @Query("keyword") keyword: String?="cruise",
        @Query("key") key: String? = "AIzaSyDbPkKDfVX8qUoyn7NR57fhuQhspM6LIjs",
    ): Response<PlaceData>

}
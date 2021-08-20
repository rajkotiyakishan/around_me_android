package com.android.aroundme.data.api

import com.android.aroundme.BuildConfig
import com.android.aroundme.data.model.GoogleMapDirection
import com.android.aroundme.data.model.PlaceData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {

    @GET("place/nearbysearch/json")
    suspend fun getNearbyPlace(
        @Query("location") location: String,
        @Query("radius") radius: String? = "10000",
        @Query("type") type: String? = "restaurant",
        @Query("key") key: String? = BuildConfig.PLACE_API_KEY,
    ): Response<PlaceData>

    @GET("directions/json?")
    suspend fun getMapRoute(@QueryMap data: HashMap<String, String>): Response<GoogleMapDirection>

}
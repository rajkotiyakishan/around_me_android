package com.android.aroundme.data.api

import com.android.aroundme.data.model.GoogleMapDirection
import com.android.aroundme.data.model.PlaceData
import retrofit2.Response

interface ApiHelper {

    suspend fun getNearbyPlace(location : String): Response<PlaceData>
    suspend fun getMapRoute(data: HashMap<String, String>): Response<GoogleMapDirection>
}
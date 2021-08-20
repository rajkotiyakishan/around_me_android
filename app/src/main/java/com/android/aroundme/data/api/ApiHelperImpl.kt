package com.android.aroundme.data.api

import com.android.aroundme.data.model.GoogleMapDirection
import com.android.aroundme.data.model.PlaceData
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {

    override suspend fun getNearbyPlace(location : String): Response<PlaceData> = apiService.getNearbyPlace(location)
    override suspend fun getMapRoute(data: HashMap<String, String>): Response<GoogleMapDirection> = apiService.getMapRoute(data)
}
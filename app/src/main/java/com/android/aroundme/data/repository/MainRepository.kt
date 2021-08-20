package com.android.aroundme.data.repository

import com.android.aroundme.data.api.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun getNearbyPlace(location : String) =  apiHelper.getNearbyPlace(location)
    suspend fun getMapRoute(data: HashMap<String, String>) =  apiHelper.getMapRoute(data)

}
package com.android.aroundme.ui.placeDirection.viewmodel

import android.database.Observable
import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.android.aroundme.BuildConfig
import com.android.aroundme.data.model.GoogleMapDirection
import com.android.aroundme.data.model.Places
import com.android.aroundme.data.repository.MainRepository
import com.android.aroundme.utils.NetworkHelper
import com.android.aroundme.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceDirectionVM @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {


    private val _route = MutableLiveData<Resource<GoogleMapDirection>>()
    val route: LiveData<Resource<GoogleMapDirection>>
        get() = _route


    fun fetchRoute(
        currentLatitude: Double?,
        currentLongitude: Double?,
        destinationLatitude: Double?,
        destinationLongitude: Double?
    ) {
        val data = HashMap<String, String>()
        data["origin"] = "$currentLatitude,$currentLongitude"
        data["destination"] = "$destinationLatitude,$destinationLongitude"
        data["mode"] = "driving"
        data["sensor"] = "false"
        data["key"] = BuildConfig.PLACE_API_KEY
        viewModelScope.launch {
            _route.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getMapRoute(data).let {
                    if (it.isSuccessful) {
                        _route.postValue(Resource.success(it.body()))
                    } else _route.postValue(Resource.error(it.errorBody().toString(), null))
                }
            } else _route.postValue(Resource.error("No internet connection", null))
        }
    }
}
package com.android.aroundme.ui.main.viewmodel

import android.database.Observable
import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.android.aroundme.CoreApp
import com.android.aroundme.R
import com.android.aroundme.data.model.Places
import com.android.aroundme.data.repository.MainRepository
import com.android.aroundme.utils.NetworkHelper
import com.android.aroundme.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    val place = ObservableField<Places>()
    private val _places = MutableLiveData<Resource<List<Places>>>()
    val places: LiveData<Resource<List<Places>>>
        get() = _places


      fun fetchPlaces(location : String) {
        viewModelScope.launch {
            _places.postValue(Resource.loading(null))
            delay(1200)
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getNearbyPlace(location).let {
                    if (it.isSuccessful) {
                           if(it.body()?.placeList!!.isNotEmpty()){
                               _places.postValue(Resource.success(it.body()?.placeList))
                           }else{
                               _places.postValue(Resource.error(CoreApp.mInstance!!.resources.getString(R.string.no_restaurant), null))
                           }

                    } else _places.postValue(Resource.error(it.errorBody().toString(), null))
                }
            } else _places.postValue(Resource.error(CoreApp.mInstance!!.resources.getString(R.string.no_internet), null))
        }
    }
}
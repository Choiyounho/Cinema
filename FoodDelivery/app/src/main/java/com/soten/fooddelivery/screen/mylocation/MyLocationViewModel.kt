package com.soten.fooddelivery.screen.mylocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soten.fooddelivery.R
import com.soten.fooddelivery.data.entity.LocationLatLngEntity
import com.soten.fooddelivery.data.entity.MapSearchInformationEntity
import com.soten.fooddelivery.data.repository.map.MapRepository
import com.soten.fooddelivery.screen.base.BaseViewModel
import com.soten.fooddelivery.screen.main.home.HomeState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyLocationViewModel(
    private val mapSearchInformationEntity: MapSearchInformationEntity,
    private val mapRepository: MapRepository
) : BaseViewModel() {

    private val _myLocationStateLiveData =
        MutableLiveData<MyLocationState>(MyLocationState.Uninitialized)
    val myLocationStateLiveData: LiveData<MyLocationState> = _myLocationStateLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _myLocationStateLiveData.value = MyLocationState.Loading
        _myLocationStateLiveData.value = MyLocationState.Success(mapSearchInformationEntity)
    }

    fun changeLocationInformation(
        locationLatLngEntity: LocationLatLngEntity
    ) = viewModelScope.launch {
        val addressInfo = mapRepository.getReverseGeoInformation(locationLatLngEntity)
        addressInfo?.let { information ->
            _myLocationStateLiveData.value = MyLocationState.Success(
                mapSearchInformationEntity = information.toSearchInfoEntity(locationLatLngEntity)
            )
        } ?: run {
            _myLocationStateLiveData.value = MyLocationState.Error(
                R.string.cannot_load_address
            )
        }
    }

    fun confirmSelectLocation() = viewModelScope.launch {
        when (val data = _myLocationStateLiveData.value) {
            is MyLocationState.Success -> {
                _myLocationStateLiveData.value = MyLocationState.Confirm(
                    data.mapSearchInformationEntity
                )
            }
        }
    }
   
}
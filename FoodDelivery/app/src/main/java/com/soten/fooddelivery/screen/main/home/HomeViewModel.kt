package com.soten.fooddelivery.screen.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soten.fooddelivery.R
import com.soten.fooddelivery.data.entity.LocationLatLngEntity
import com.soten.fooddelivery.data.repository.map.MapRepository
import com.soten.fooddelivery.screen.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mapRepository: MapRepository
) : BaseViewModel() {

    private val _homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)

    val homeStateLiveData: LiveData<HomeState> = _homeStateLiveData

    fun loadReverseGeoInformation(locationLatLngEntity: LocationLatLngEntity) =
        viewModelScope.launch {
            _homeStateLiveData.value = HomeState.Loading
            val addressInfo = mapRepository.getReverseGeoInformation(locationLatLngEntity)
            addressInfo?.let { information ->
                _homeStateLiveData.value = HomeState.Success(
                    information.toSearchInfoEntity(locationLatLngEntity)
                )
            } ?: run {
                _homeStateLiveData.value = HomeState.Error(
                    R.string.cannot_load_address
                )
            }
        }
}


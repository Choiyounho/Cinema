package com.soten.fooddelivery.screen.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soten.fooddelivery.R
import com.soten.fooddelivery.data.entity.LocationLatLngEntity
import com.soten.fooddelivery.data.entity.MapSearchInformationEntity
import com.soten.fooddelivery.data.repository.map.MapRepository
import com.soten.fooddelivery.data.repository.user.UserRepository
import com.soten.fooddelivery.screen.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mapRepository: MapRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)

    val homeStateLiveData: LiveData<HomeState> = _homeStateLiveData

    fun loadReverseGeoInformation(locationLatLngEntity: LocationLatLngEntity) =
        viewModelScope.launch {
            _homeStateLiveData.value = HomeState.Loading
            val userLocation = userRepository.getUserLocation()
            val currentLocation = userLocation ?: locationLatLngEntity

            val addressInfo = mapRepository.getReverseGeoInformation(currentLocation)
            addressInfo?.let { information ->
                _homeStateLiveData.value = HomeState.Success(
                    information.toSearchInfoEntity(locationLatLngEntity),
                    currentLocation == locationLatLngEntity
                )
            } ?: run {
                _homeStateLiveData.value = HomeState.Error(
                    R.string.cannot_load_address
                )
            }
        }

    fun getMapSearchInformation(): MapSearchInformationEntity? {
        when (val data = _homeStateLiveData.value) {
            is HomeState.Success -> {
                return data.mapSearchInformationEntity
            }
        }

        return null
    }

    companion object {
        const val MY_LOCATION_KEY = "MY_LOCATION_KEY"
    }
}


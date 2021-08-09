package com.soten.fooddelivery.screen.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.soten.fooddelivery.screen.base.BaseViewModel

class HomeViewModel : BaseViewModel() {

    private val _homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)
    val homeStateLiveData: LiveData<HomeState> = _homeStateLiveData

}
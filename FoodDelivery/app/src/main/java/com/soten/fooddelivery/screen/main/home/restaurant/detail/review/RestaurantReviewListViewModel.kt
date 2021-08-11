package com.soten.fooddelivery.screen.main.home.restaurant.detail.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soten.fooddelivery.data.repository.restaurant.review.RestaurantReviewRepository
import com.soten.fooddelivery.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantReviewListViewModel(
    private val restaurantTitle: String,
    private val restaurantRepository: RestaurantReviewRepository
) : BaseViewModel() {

    private val _reviewStateLiveData = MutableLiveData<RestaurantReviewState>(RestaurantReviewState.Uninitialized)
    val reviewStateLiveData: LiveData<RestaurantReviewState> = _reviewStateLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _reviewStateLiveData.value = RestaurantReviewState.Loading
        val reviews = restaurantRepository.getReviews(restaurantTitle)
        _reviewStateLiveData.value = RestaurantReviewState.Success(
            reviews
        )
    }

}
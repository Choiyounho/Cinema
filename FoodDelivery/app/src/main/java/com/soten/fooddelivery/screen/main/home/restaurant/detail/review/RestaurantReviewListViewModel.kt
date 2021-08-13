package com.soten.fooddelivery.screen.main.home.restaurant.detail.review

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soten.fooddelivery.data.entity.ReviewEntity
import com.soten.fooddelivery.data.repository.restaurant.review.RestaurantReviewRepository
import com.soten.fooddelivery.data.repository.restaurant.review.ReviewResult
import com.soten.fooddelivery.model.review.RestaurantReviewModel
import com.soten.fooddelivery.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantReviewListViewModel(
    private val restaurantTitle: String,
    private val restaurantRepository: RestaurantReviewRepository
) : BaseViewModel() {

    private val _reviewStateLiveData =
        MutableLiveData<RestaurantReviewState>(RestaurantReviewState.Uninitialized)
    val reviewStateLiveData: LiveData<RestaurantReviewState> = _reviewStateLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _reviewStateLiveData.value = RestaurantReviewState.Loading
        val result = restaurantRepository.getReviews(restaurantTitle)

        when (result) {
            is ReviewResult.Success<*> -> {
                val reviews = result.data as List<ReviewEntity>
                _reviewStateLiveData.value = RestaurantReviewState.Success(
                    reviews.map {
                        RestaurantReviewModel(
                            id = it.hashCode().toLong(),
                            title = it.title,
                            description = it.content,
                            grade = it.rating,
                            thumbnailImageUri = if (it.imageUrlList.isNullOrEmpty()) {
                                null
                            } else {
                                Uri.parse(it.imageUrlList.first())
                            }
                        )
                    }
                )
            }
            else -> {}
        }
    }

}
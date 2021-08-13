package com.soten.fooddelivery.screen.main.home.restaurant.detail.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soten.fooddelivery.data.entity.RestaurantFoodEntity
import com.soten.fooddelivery.data.repository.restaurant.food.RestaurantFoodRepository
import com.soten.fooddelivery.model.food.FoodModel
import com.soten.fooddelivery.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantMenuListViewModel(
    private val restaurantId: Long,
    private val foodEntityList: List<RestaurantFoodEntity>,
    private val restaurantFoodRepository: RestaurantFoodRepository
) : BaseViewModel() {

    private val _menuBasketLiveData = MutableLiveData<RestaurantFoodEntity>()
    val menuBasketLiveData: LiveData<RestaurantFoodEntity> = _menuBasketLiveData

    private val _restaurantFoodListLiveData = MutableLiveData<List<FoodModel>>()
    val restaurantFoodListLiveData: LiveData<List<FoodModel>> = _restaurantFoodListLiveData

    private val _isClearNeedInBasketLiveData = MutableLiveData<Pair<Boolean, () -> Unit>>()
    val isClearNeedInBasketLiveData: LiveData<Pair<Boolean, () -> Unit>> =
        _isClearNeedInBasketLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _restaurantFoodListLiveData.value = foodEntityList.map {
            FoodModel(
                id = it.hashCode().toLong(),
                title = it.title,
                description = it.description,
                price = it.price,
                imageUrl = it.imageUrl,
                restaurantId = restaurantId,
                foodId = it.id,
                restaurantTitle = it.restaurantTitle
            )
        }
    }

    fun insertMenuInBasket(foodModel: FoodModel) = viewModelScope.launch {
        val restaurantMenuListInBasket =
            restaurantFoodRepository.getFoodMenuListInBasket(restaurantId)
        val foodMenuEntity = foodModel.toEntity(restaurantMenuListInBasket.size)
        val anotherRestaurantMenuListBasket =
            restaurantFoodRepository.getAllFoodMenuListInBasket()
                .filter { it.restaurantId != restaurantId }

        if (anotherRestaurantMenuListBasket.isNotEmpty()) {
            _isClearNeedInBasketLiveData.value = Pair(true, { clearMenuAndInsertNewMenuInBasket(foodMenuEntity) })
        } else {
            restaurantFoodRepository.insertFoodMenuInBasket(foodMenuEntity)
            _menuBasketLiveData.value = foodMenuEntity
        }

    }

    private fun clearMenuAndInsertNewMenuInBasket(foodMenuEntity: RestaurantFoodEntity) =
        viewModelScope.launch {
            restaurantFoodRepository.clearFoodMenuListInBasket()
            restaurantFoodRepository.insertFoodMenuInBasket(foodMenuEntity)
            _menuBasketLiveData.value = foodMenuEntity
        }
}
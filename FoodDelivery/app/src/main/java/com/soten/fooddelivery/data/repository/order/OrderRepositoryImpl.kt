package com.soten.fooddelivery.data.repository.order

import com.google.firebase.firestore.FirebaseFirestore
import com.soten.fooddelivery.data.entity.RestaurantFoodEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class OrderRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val firestore: FirebaseFirestore
) : OrderRepository {

    override suspend fun orderMenu(
        userId: String,
        restaurantId: Long,
        foodMenuList: List<RestaurantFoodEntity>
    ) : Result = withContext(ioDispatcher) {
        val result: Result
        val orderMenuData = hashMapOf(
            "restaurantId" to restaurantId,
            "userId" to userId,
            "orderMenuList" to foodMenuList
        )
        result = try {
            firestore
                .collection("order")
                .add(orderMenuData)
            Result.Success<Any>()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }
        return@withContext result
    }

    sealed class Result {
        data class Success<T>(
            val data: T? = null
        ) : Result()

        data class Error(
            val e: Throwable
        ) : Result()
    }
}
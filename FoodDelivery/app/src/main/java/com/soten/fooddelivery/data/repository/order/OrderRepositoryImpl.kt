package com.soten.fooddelivery.data.repository.order

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.soten.fooddelivery.data.entity.OrderEntity
import com.soten.fooddelivery.data.entity.RestaurantFoodEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class OrderRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val firestore: FirebaseFirestore
) : OrderRepository {

    override suspend fun orderMenu(
        userId: String,
        restaurantId: Long,
        foodMenuList: List<RestaurantFoodEntity>
    ) : OrderResult = withContext(ioDispatcher) {
        val orderResult: OrderResult
        val orderMenuData = hashMapOf(
            "restaurantId" to restaurantId,
            "userId" to userId,
            "orderMenuList" to foodMenuList
        )
        orderResult = try {
            firestore
                .collection("order")
                .add(orderMenuData)
            OrderResult.Success<Any>()
        } catch (e: Exception) {
            e.printStackTrace()
            OrderResult.Error(e)
        }
        return@withContext orderResult
    }

    override suspend fun getAllOrderMenus(userId: String): OrderResult = withContext(ioDispatcher) {
        return@withContext try {
            val result: QuerySnapshot = firestore
                .collection("order")
                .whereEqualTo("userId", userId)
                .get()
                .await()
            OrderResult.Success(result.documents.map {
                OrderEntity(
                    id = it.id,
                    userId = it.get("userId") as String,
                    restaurantId = it.get("restaurantId") as Long,
                    foodMenuList = (it.get("orderMenuList") as ArrayList<Map<String, Any>>).map { food ->
                        RestaurantFoodEntity(
                            id = food["id"] as String,
                            title = food["title"] as String,
                            description = food["description"] as String,
                            price = (food["price"] as Long).toInt(),
                            imageUrl = food["imageUrl"] as String,
                            restaurantId = food["restaurantId"] as Long
                        )
                    }
                )
            })
        } catch (e: Exception) {
            e.printStackTrace()
            OrderResult.Error(e)
        }
    }

}
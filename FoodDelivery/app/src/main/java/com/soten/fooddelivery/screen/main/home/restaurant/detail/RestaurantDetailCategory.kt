package com.soten.fooddelivery.screen.main.home.restaurant.detail

import androidx.annotation.StringRes
import com.soten.fooddelivery.R

enum class RestaurantDetailCategory(
    @StringRes val categoryNameId: Int,
) {

    MENU(R.string.menu),
    REVIEW(R.string.review)

}
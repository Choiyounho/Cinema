package com.soten.fooddelivery.model.review

import android.net.Uri
import com.soten.fooddelivery.model.CellType
import com.soten.fooddelivery.model.Model

class RestaurantReviewModel(
    override val id: Long,
    override val type: CellType = CellType.REVIEW_CELL,
    val title: String,
    val description: String,
    val grade: Float,
    val thumbnailImageUri: Uri? = null
) : Model(id, type) {
}
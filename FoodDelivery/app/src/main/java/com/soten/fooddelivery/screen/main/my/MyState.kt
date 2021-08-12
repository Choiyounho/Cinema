package com.soten.fooddelivery.screen.main.my

import android.net.Uri
import androidx.annotation.StringRes

sealed class MyState {

    object Uninitialized: MyState()

    object Loading: MyState()

    data class Login(
        val idToken: String
    ): MyState()

    sealed class Success: MyState() {

        // 토큰이 있을 때
        data class Registered(
            val userName: String,
            val profileImageUri: Uri?,
//            val orderList: List<OrderModel>
        ): Success()

        // 토큰이 없을 때
        object NotRegistered: Success()

    }

    data class Error(
        @StringRes val messageId: Int,
        val e: Throwable
    ): MyState()

}

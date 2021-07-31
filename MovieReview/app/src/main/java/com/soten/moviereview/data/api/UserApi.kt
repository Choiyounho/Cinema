package com.soten.moviereview.data.api

import com.soten.moviereview.domain.model.User

interface UserApi {

    suspend fun saveUser(user: User): User
}
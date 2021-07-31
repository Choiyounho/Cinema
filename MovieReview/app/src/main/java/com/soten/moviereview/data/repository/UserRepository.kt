package com.soten.moviereview.data.repository

import com.soten.moviereview.domain.model.User

interface UserRepository {

    suspend fun getUser(): User?

    suspend fun saveUser(user: User)

}
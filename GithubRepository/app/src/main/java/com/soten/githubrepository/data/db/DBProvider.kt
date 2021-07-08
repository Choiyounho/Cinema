package com.soten.githubrepository.data.db

import android.content.Context
import androidx.room.Room

object DBProvider {

    private const val DB_NAME = "github_repository_app.db"

    fun provideDB(applicationContext: Context) = Room.databaseBuilder(
        applicationContext,
        SimpleGithubDB::class.java,
        DB_NAME
    ).build()

}
package com.soten.githubrepository.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soten.githubrepository.data.dao.RepositoryDao
import com.soten.githubrepository.data.entity.GithubRepositoryEntity

@Database(entities = [GithubRepositoryEntity::class], version = 1)
abstract class SimpleGithubDB: RoomDatabase() {

    abstract fun repositoryDao(): RepositoryDao
}
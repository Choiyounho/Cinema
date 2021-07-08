package com.soten.githubrepository.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.soten.githubrepository.data.entity.GithubRepositoryEntity

@Dao
interface SearchHistoryDao {

    @Insert
    suspend fun insert(repository: GithubRepositoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repositoryList: List<GithubRepositoryEntity>)

//    @Query("SELECT * FROM githubrepository")
//    suspend fun getHistory(): List<GithubRepositoryEntity>

    @Query("SELECT * FROM githubrepository WHERE fullName = :repoName")
    suspend fun getRepository(repoName: String): GithubRepositoryEntity?

    @Query("DELETE FROM githubrepository WHERE fullName = :repoName")
    suspend fun remove(repoName: String)

    @Query("DELETE FROM githubrepository")
    suspend fun clearAll()
}
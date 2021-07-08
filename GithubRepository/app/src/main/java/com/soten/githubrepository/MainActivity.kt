package com.soten.githubrepository

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.soten.githubrepository.data.db.DBProvider
import com.soten.githubrepository.data.entity.GithubOwner
import com.soten.githubrepository.data.entity.GithubRepositoryEntity
import com.soten.githubrepository.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val job by lazy { Job() }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: ActivityMainBinding

    private val repositoryDao by lazy { DBProvider.provideDB(applicationContext).repositoryDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        launch {
            addMockData()
            val githubRepositories = loadGithubRepositories()
            withContext(coroutineContext) {
                Log.e("githubRepositories", githubRepositories.toString())
            }
        }
    }

    private suspend fun addMockData() = withContext(Dispatchers.IO) {
        val mockData = (0 until 10).map {
            GithubRepositoryEntity(
                name = "repo $it",
                fullName = "repo $it",
                owner = GithubOwner(
                    "login",
                    "avatarUrl"
                ),
                description = null,
                language = null,
                updateAt = Date().toString(),
                stargazerCount = it
            )
        }
        repositoryDao.insertAll(mockData)
    }

    private suspend fun loadGithubRepositories() = withContext(Dispatchers.IO) {
        return@withContext repositoryDao.getHistory()
    }
}
package com.soten.githubrepository

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.soten.githubrepository.data.db.DBProvider
import com.soten.githubrepository.data.entity.GithubRepositoryEntity
import com.soten.githubrepository.databinding.ActivityMainBinding
import com.soten.githubrepository.view.RepositoryRecyclerAdapter
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RepositoryRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        initViews()
    }

    private fun initAdapter() {
        adapter = RepositoryRecyclerAdapter()
    }

    private fun initViews() = with(binding) {
        emptyResultTextView.isGone = true
        repositoryRecyclerView.adapter = adapter
        searchButton.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, SearchActivity::class.java)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        launch(coroutineContext) {
            loadSearchHistory()
        }
    }

    private suspend fun loadSearchHistory() = withContext(Dispatchers.IO) {
        val histories = DBProvider.provideDB(this@MainActivity).searchHistoryDao().getHistory()
        withContext(Dispatchers.Main) {
            setData(histories)
        }
    }

    private fun setData(githubRepoList: List<GithubRepositoryEntity>) = with(binding) {
        if (githubRepoList.isEmpty()) {
            emptyResultTextView.isGone = false
            repositoryRecyclerView.isGone = true
        } else {
            emptyResultTextView.isGone = true
            repositoryRecyclerView.isGone = false
            adapter.setSearchResultList(githubRepoList) {
                startActivity(
                    Intent(this@MainActivity, RepositoryActivity::class.java).apply {
                        putExtra(RepositoryActivity.REPOSITORY_OWNER_KEY, it.owner.login)
                        putExtra(RepositoryActivity.REPOSITORY_NAME_KEY, it.name)
                    }
                )
            }
        }
    }

}
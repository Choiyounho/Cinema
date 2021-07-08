package com.soten.githubrepository

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.soten.githubrepository.RepositoryActivity.Companion.REPOSITORY_NAME_KEY
import com.soten.githubrepository.RepositoryActivity.Companion.REPOSITORY_OWNER_KEY
import com.soten.githubrepository.data.entity.GithubRepositoryEntity
import com.soten.githubrepository.databinding.ActivitySearchBinding
import com.soten.githubrepository.utility.RetrofitUtil
import com.soten.githubrepository.view.RepositoryRecyclerAdapter
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SearchActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: RepositoryRecyclerAdapter

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        initViews()
        bindViews()
    }

    private fun initAdapter() {
        adapter = RepositoryRecyclerAdapter()
    }

    private fun initViews() = with(binding) {
        emptyResultTextView.isGone = true
        searchRecyclerView.adapter = adapter
    }

    private fun bindViews() = with(binding) {
        searchButton.setOnClickListener {
            searchKeyword(searchBarInputView.text.toString())
        }
    }

    private fun searchKeyword(ketword: String) {
        showLoading(true)
        launch(coroutineContext) {
            try {
                withContext(Dispatchers.IO) {
                    val response = RetrofitUtil.githubApiService.searchRepositories(
                        query = ketword
                    )
                    if (response.isSuccessful) {
                        val body = response.body()
                        withContext(Dispatchers.Main) {
                            body?.let { searchResponse ->
                                setData(searchResponse.items)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@SearchActivity, "검색하는 과정에서 에러가 발생했습니다. : ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setData(githubRepositoryList: List<GithubRepositoryEntity>) = with(binding) {
        showLoading(false)
        if (githubRepositoryList.isEmpty()) {
            emptyResultTextView.isGone = false
            searchRecyclerView.isGone = true
        } else {
            emptyResultTextView.isGone = true
            searchRecyclerView.isGone = false
            adapter.setSearchResultList(githubRepositoryList) {
                startActivity(
                    Intent(this@SearchActivity, RepositoryActivity::class.java).apply {
                        putExtra(REPOSITORY_OWNER_KEY, it.owner.login)
                        putExtra(REPOSITORY_NAME_KEY, it.name)
                    }
                )
            }
        }
    }

    private fun showLoading(isShown: Boolean) = with(binding) {
        progressBar.isGone = isShown.not()
    }

}
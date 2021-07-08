package com.soten.githubrepository

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.soten.githubrepository.data.entity.GithubRepositoryEntity
import com.soten.githubrepository.databinding.ActivitySearchBinding
import com.soten.githubrepository.utility.RetrofitUtil.githubApiService
import com.soten.githubrepository.view.RepositoryRecyclerAdapter
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SearchActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivitySearchBinding

    private val job by lazy { Job() }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var adapter: RepositoryRecyclerAdapter

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
        RepositoryRecyclerView.adapter = adapter
    }

    private fun bindViews() = with(binding) {
        searchButton.setOnClickListener {
            searchKeyword(searchInputText.text.toString())
            Log.e("responsee", searchInputText.text.toString())
        }
    }

    private fun searchKeyword(keyword: String) = launch {
        withContext(Dispatchers.IO) {
            val response = githubApiService.searchRepositories(keyword)
            if (response.isSuccessful) {
                val body = response.body()
                withContext(Dispatchers.Main) {
                    body?.let {
                        setData(it.items)
                    }
                }
            }
        }
    }

    private fun setData(items: List<GithubRepositoryEntity>) {
        Log.e("responsee", items.toString())

        adapter.setSearchResultList (items) {
            Toast.makeText(this@SearchActivity, "클릭", Toast.LENGTH_SHORT).show()
        }
    }
}
package com.soten.imagesearching

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soten.imagesearching.data.Repository
import com.soten.imagesearching.databinding.ActivityMainBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private val scope = MainScope()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        bindView()
        fetchRandomPhotos()
    }

    private fun initViews() = with(binding) {
        recyclerView.layoutManager =
            LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        recyclerView.adapter = PhotoAdapter()
    }

    private fun bindView() = with(binding) {
        searchEditText.setOnEditorActionListener { editText, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                currentFocus?.let { view ->
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
                    view.clearFocus()
                }
                fetchRandomPhotos(editText.text.toString())
            }
            true
        }

        refreshLayout.setOnRefreshListener {
            fetchRandomPhotos(binding.searchEditText.text.toString())
        }
    }

    private fun fetchRandomPhotos(query: String? = null) = scope.launch {
        try {
            Repository.getRandomPhotos(query)?.let { photos ->
                binding.errorDescriptionTextView.visibility = View.GONE
                (binding.recyclerView.adapter as? PhotoAdapter)?.apply {
                    this.photos = photos
                    notifyDataSetChanged()
                }
                binding.recyclerView.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            binding.recyclerView.visibility = View.INVISIBLE
            binding.errorDescriptionTextView.visibility = View.VISIBLE
        } finally {
            binding.shimmerLayout.visibility = View.GONE
            binding.refreshLayout.isRefreshing = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
package com.soten.locationsearch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.soten.locationsearch.databinding.ActivityMainBinding
import com.soten.locationsearch.model.LocationLatLngEntity
import com.soten.locationsearch.model.SearchResultEntity
import com.soten.locationsearch.respose.search.Poi
import com.soten.locationsearch.respose.search.Pois
import com.soten.locationsearch.utility.RetrofitUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SearchRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()

        initAdapter()
        initViews()
        bindViews()
        initData()
    }

    private fun initAdapter() {
        adapter = SearchRecyclerViewAdapter()
    }

    private fun initViews() = with(binding) {
        emptyResultTextView.isVisible = false
        searchRecyclerView.adapter = adapter
    }

    private fun bindViews() = with(binding) {
        searchButton.setOnClickListener {
            searchKeyword(searchBarInputText.text.toString())
        }
    }

    private fun initData() {
        adapter.notifyDataSetChanged()
    }

    private fun setData(pois: Pois) {
        val dataList = pois.poi.map {
            SearchResultEntity(
                fullAddress = makeMainAddress(it),
                name = it.name ?: "",
                locationLatLng = LocationLatLngEntity(it.noorLat, it.noorLon)
            )
        }
        adapter.setSearchResultListener(dataList) {
            startActivity(
                Intent(this, MapActivity::class.java).apply {
                    Log.d("TestT", SEARCH_RESULT_EXTRA_KEY)
                    putExtra(SEARCH_RESULT_EXTRA_KEY, it)
                }
            )
        }
    }

    private fun searchKeyword(keywordString: String) {
        launch(coroutineContext) {
            try {
                withContext(Dispatchers.IO) {
                    val response = RetrofitUtil.apiService.getSearchLocation(
                        keyword = keywordString
                    )
                    if (response.isSuccessful) {
                        val body = response.body()
                        withContext(Dispatchers.Main) {
                            Log.e("list", body.toString())
                            body?.let { searchResponseSchema ->
                                setData(searchResponseSchema.searchPoiInfo.pois)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, "검색하는 과정에서 에러가 발생했습니다. : ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun makeMainAddress(poi: Poi): String =
        if (poi.secondNo?.trim().isNullOrEmpty()) {
            (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.middleAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    poi.firstNo?.trim()
        } else {
            (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.middleAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    (poi.firstNo?.trim() ?: "") + " " +
                    poi.secondNo?.trim()
        }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        const val SEARCH_RESULT_EXTRA_KEY = "SEARCH_RESULT_EXTRA_KEY" //"SearchResult"
    }
}
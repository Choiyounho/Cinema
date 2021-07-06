package com.soten.locationsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.soten.locationsearch.databinding.ActivityMainBinding
import com.soten.locationsearch.model.LocationLatLngEntity
import com.soten.locationsearch.model.SearchResultEntity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SearchRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        initViews()
        initData()

        setData()
    }

    private fun initAdapter() {
        adapter = SearchRecyclerViewAdapter()
    }

    private fun initViews() = with(binding) {
        emptyResultTextView.isVisible = false
        searchRecyclerView.adapter = adapter
    }

    private fun initData() {
        adapter.notifyDataSetChanged()
    }

    private fun setData() {
        val dataList = (0..10).map {
            SearchResultEntity(
                name = "빌딩 $it",
                fullAddress = "address $it",
                locationLatLng = LocationLatLngEntity(
                    it.toFloat(),
                    it.toFloat()
                )
            )
        }

        adapter.setSearchResultListener(dataList) {
            Toast.makeText(this, "빌딩이름 : ${it.name} 주소 : ${it.fullAddress}", Toast.LENGTH_SHORT).show()
        }
    }
}
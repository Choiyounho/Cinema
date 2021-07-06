package com.soten.locationsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soten.locationsearch.databinding.ItemSearchBinding
import com.soten.locationsearch.model.SearchResultEntity

class SearchRecyclerViewAdapter : RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder>() {

    private var itemList: List<SearchResultEntity> = listOf()
    private lateinit var itemClickedListener: (SearchResultEntity) -> Unit

    class ViewHolder(
        private val binding: ItemSearchBinding,
        val itemClickedListener: (SearchResultEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SearchResultEntity) = with(binding) {
            titleTextView.text = data.name
            subtitleTextView.text = data.fullAddress
        }

        fun bindViews(data: SearchResultEntity) {
            binding.root.setOnClickListener {
                itemClickedListener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view, itemClickedListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
        holder.bindViews(itemList[position])
    }

    override fun getItemCount() = itemList.size

    fun setSearchResultListener(
        itemList: List<SearchResultEntity>,
        itemClickedListener: (SearchResultEntity) -> Unit
    ) {
        this.itemList = itemList
        this.itemClickedListener = itemClickedListener
        notifyDataSetChanged()
    }
}
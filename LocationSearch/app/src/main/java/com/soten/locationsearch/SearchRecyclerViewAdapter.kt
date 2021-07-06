package com.soten.locationsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soten.locationsearch.databinding.ItemSearchBinding

class SearchRecyclerViewAdapter: RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder>() {

    private var itemList: List<Any> = listOf()
    private lateinit var itemClickedListener: (Any) -> Unit

    class ViewHolder(private val binding: ItemSearchBinding, val itemClickedListener: (Any) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Any) = with(binding) {
            titleTextView.text = "제목"
            subtitleTextView.text = "부제목"
        }

        fun bindViews(data: Any) {
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
        holder.bind(Any())
        holder.bindViews(Any())
    }

    override fun getItemCount() = 10

    fun setSearchResultListener(itemList: List<Any>, itemClickedListener: (Any) -> Unit) {
        this.itemList = itemList
        this.itemClickedListener = itemClickedListener
    }
}
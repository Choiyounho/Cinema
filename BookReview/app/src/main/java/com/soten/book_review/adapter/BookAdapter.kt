package com.soten.book_review.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soten.book_review.model.book.Book
import com.soten.review.databinding.ItemBookBinding

/**
 * diffUtil : 리사이클러 뷰가 실제로 뷰의 포지션이 바꼈을 때 할당할 기준
 */

class BookAdapter(val clickListener: (Book) -> Unit) : ListAdapter<Book, BookAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bookModel: Book) {
            binding.titleTextView.text = bookModel.title
            binding.descriptionTextView.text = bookModel.description

            Glide
                .with(binding.coverImageView.context)
                .load(bookModel.coverSmallUrl)
                .into(binding.coverImageView)

            binding.root.setOnClickListener {
                clickListener(bookModel)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Book>() {
            override fun areContentsTheSame(oldItem: Book, newItem: Book) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Book, newItem: Book) =
                oldItem.id == newItem.id
        }
    }

}
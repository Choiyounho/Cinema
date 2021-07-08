package com.soten.githubrepository.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.soten.githubrepository.data.entity.GithubRepositoryEntity
import com.soten.githubrepository.databinding.ItemRepositoryBinding
import com.soten.githubrepository.extensions.loadCenterInside

class RepositoryRecyclerAdapter : RecyclerView.Adapter<RepositoryRecyclerAdapter.RepositoryItemViewHolder>() {

    private var repositoryList: List<GithubRepositoryEntity> = listOf()
    private lateinit var repositoryClickListener: (GithubRepositoryEntity) -> Unit

    inner class RepositoryItemViewHolder(
        private val binding: ItemRepositoryBinding,
        val searchResultClickListener: (GithubRepositoryEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(data: GithubRepositoryEntity) = with(binding) {
            ownerProfileImageView.loadCenterInside(data.owner.avatarUrl, 24f)
            ownerNameTextView.text = data.owner.login
            nameTextView.text = data.fullName
            subtextTextView.text = data.description
            stargazersCountText.text = data.stargazersCount.toString()
            data.language?.let { language ->
                languageText.isGone = false
                languageText.text = language
            } ?: kotlin.run {
                languageText.isGone = true
                languageText.text = ""
            }
        }

        fun bindViews(data: GithubRepositoryEntity) {
            binding.root.setOnClickListener {
                searchResultClickListener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryItemViewHolder {
        val view = ItemRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepositoryItemViewHolder(view, repositoryClickListener)
    }

    override fun onBindViewHolder(holder: RepositoryItemViewHolder, position: Int) {
        holder.bindData(repositoryList[position])
        holder.bindViews(repositoryList[position])
    }

    override fun getItemCount(): Int = repositoryList.size

    fun setSearchResultList(searchResultList: List<GithubRepositoryEntity>, searchResultClickListener: (GithubRepositoryEntity) -> Unit) {
        this.repositoryList = searchResultList
        this.repositoryClickListener = searchResultClickListener
        notifyDataSetChanged()
    }
}
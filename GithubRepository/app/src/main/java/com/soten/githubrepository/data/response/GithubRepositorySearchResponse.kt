package com.soten.githubrepository.data.response

import com.soten.githubrepository.data.entity.GithubRepositoryEntity

data class GithubRepositorySearchResponse(
    val totalCount: Int,
    val items: List<GithubRepositoryEntity>
)
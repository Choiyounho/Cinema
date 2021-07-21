package com.soten.sns.ui.home

data class ArticleModel(
    val sellerId: String,
    val title: String,
    val createdAt: Long,
    val content: String,
    val imageUrl: String
) {
    constructor() : this("", "", 0, "", "")
}
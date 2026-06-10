package com.trinoka.bharatnews.data.model

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val results: List<Article>,
    val nextPage: String?
)
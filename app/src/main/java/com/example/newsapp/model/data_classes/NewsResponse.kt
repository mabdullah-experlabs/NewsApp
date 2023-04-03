package com.example.newsapp.model.data_classes

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)
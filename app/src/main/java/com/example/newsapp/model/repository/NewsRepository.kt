package com.example.newsapp.model.repository

import com.example.newsapp.model.api.NewsAPI
import com.example.newsapp.model.data_classes.Article
import com.example.newsapp.model.db.ArticleDao
import kotlinx.coroutines.flow.Flow

class NewsRepository(
    private var api: NewsAPI,
    private var db: ArticleDao
) {

    suspend fun getBreakingNews(countryCode:String,pageNumber:Int) =
        api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int)=
        api.getSearchedNews(searchQuery,pageNumber)

    suspend fun upsert(article: Article)=db.upsert(article)

    fun getSavedArticles(): Flow<List<Article>> = db.getAllArticles()

    suspend fun delete(article: Article)=db.delete(article)



}
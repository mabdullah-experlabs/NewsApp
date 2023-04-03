package com.example.newsapp.model.api

import com.example.newsapp.model.data_classes.NewsResponse
import com.example.newsapp.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    //to get news i use coroutines with will start with suspend key word
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        // if to add a parameter to request url then we use @QUERY
        @Query("country") countryCode: String = "pk",
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY
    ):Response<NewsResponse>

    @GET("v2/everything")
    suspend fun getSearchedNews(
        // if to add a parameter to request url then we use @QUERY
        @Query("q") searchQuery: String,
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY
    ):Response<NewsResponse>

}
package com.example.newsapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.data_classes.Article
import com.example.newsapp.model.data_classes.NewsResponse
import com.example.newsapp.model.repository.NewsRepository
import com.example.newsapp.utils.Countries
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

//to check in internet is on we use application context instead of passing activity context which is against mvvm principles
// to do that we need AndroidViewModel instead of ViewModel
class NewsViewModel(
    var app: Application, private var newsRepository: NewsRepository
) : AndroidViewModel(app) {

    private val _breakingNews = MutableStateFlow<Resource<NewsResponse>>(Resource.Loading())
    val breakingNews = _breakingNews.asStateFlow()
    private var breakingNewsPage = 1
    private var countryCode = "us"


    private val _searchNews = MutableStateFlow<Resource<NewsResponse>>(Resource.Loading())
    val searchNews = _searchNews.asStateFlow()
    private var searchNewsPage = 1


    init {
        getBreakingNews(Countries.UnitedStatesOfAmerica.country)
    }

    // getting data through api and passing it through State Flows =================================

    private fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    //handling exception when getting data from api
    private suspend fun safeBreakingNewsCall(countryCode: String) {
        _breakingNews.value = Resource.Loading()
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                _breakingNews.value = newsResponseHandler(response)
            } else {
                _breakingNews.value = Resource.Error("No Internet Connection")
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> _breakingNews.value = Resource.Error("Network Failure")
                else -> _breakingNews.value = Resource.Error("Conversion Error")
            }

        }

    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    //handling exception when getting data from api
    private suspend fun safeSearchNewsCall(searchQuery: String) {
        _searchNews.value = Resource.Loading()
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                _searchNews.value = newsResponseHandler(response)
            } else {
                _searchNews.value = Resource.Error("No Internet Connection")
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> _searchNews.value = Resource.Error("Network failure")
                else -> _searchNews.value = Resource.Error("Conversion Error")
            }

        }

    }

    private fun newsResponseHandler(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    //Sending and Receiving Data from Room Database

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedArticles(): Flow<List<Article>> = newsRepository.getSavedArticles()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.delete(article)
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }

    }

}
package com.example.newsapp.utils

class Constants {
    companion object{
        const val API_KEY: String = "59fdde2338254214a580f0bda9259bf4"
        const val BASE_URL:String = "https://newsapi.org"
        const val SEARCH_NEWS_TIME_DELAY =500L
    }
}
enum class Countries(val country:String){
    UnitedStatesOfAmerica("us")
}
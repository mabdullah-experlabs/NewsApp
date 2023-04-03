package com.example.newsapp.koin

import androidx.room.Room
import com.example.newsapp.model.api.NewsAPI
import com.example.newsapp.model.db.ArticleDatabase
import com.example.newsapp.model.repository.NewsRepository
import com.example.newsapp.utils.Constants
import com.example.newsapp.viewmodel.NewsViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val appModule = module{
    single{
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build())
            .build()
            .create(NewsAPI::class.java)
    }
    single{
        Room.databaseBuilder(
            get(),
            ArticleDatabase::class.java,
            "article_db.db"
        ).build()
    }
    single{
        get<ArticleDatabase>().getArticleDao()
    }

    single{
        NewsRepository(get(),get())
    }

    viewModel{
        NewsViewModel(androidApplication(),get())
    }


}
package com.example.newsapp.model.db

import androidx.room.*
import com.example.newsapp.model.data_classes.Article
import kotlinx.coroutines.flow.Flow

//making a room data access object which will insert/update, delete and get(live data) data(Articles) from table "articles"
@Dao
interface ArticleDao {

    //this will insert or update and return the id of article in long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article):Long

    // this returns live data which does not pair well with coroutine suspend function
    @Query("SELECT * FROM articles")
    fun getAllArticles(): Flow<List<Article>>

    @Delete
    suspend fun delete(article: Article)
}
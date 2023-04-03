package com.example.newsapp.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.model.data_classes.Article

//creating a room database, we need to tell what tables are included in the database and whats the version so if we change
// database the room knows to migrate from old database
@Database(
    entities = [Article::class],
    version = 1,
    exportSchema = false
)
abstract class ArticleDatabase : RoomDatabase() {

    // this will initialize and get the Dao on its own
    abstract fun getArticleDao(): ArticleDao

}
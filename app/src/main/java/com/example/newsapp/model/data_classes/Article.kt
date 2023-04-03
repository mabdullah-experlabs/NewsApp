package com.example.newsapp.model.data_classes

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.newsapp.model.db.Converter
import java.io.Serializable

//making this class a room database table
// room can only handle primitive data types so we need type converter to tell room how to interpret
// "source" type so we need type converter class and o and from source logic and tell room about it
@Entity(
    tableName = "articles"
)
@TypeConverters(
    Converter::class
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String? = null,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
): Serializable
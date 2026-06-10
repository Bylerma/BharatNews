package com.trinoka.bharatnews.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val article_id: String,
    val title: String,
    val description: String?,
    val link: String,
    val image_url: String?,
    val source_name: String?,
    val pubDate: String?,
    val savedAt: Long = System.currentTimeMillis()
)
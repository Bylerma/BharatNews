package com.trinoka.bharatnews.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.trinoka.bharatnews.data.model.BookmarkEntity

@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: BookmarkEntity)

    @Query("SELECT * FROM bookmarks ORDER BY savedAt DESC")
    fun getAllBookmarks(): LiveData<List<BookmarkEntity>>

    @Delete
    suspend fun deleteBookmark(article: BookmarkEntity)

    @Query("SELECT EXISTS(SELECT * FROM bookmarks WHERE article_id = :articleId)")
    suspend fun isBookmarked(articleId: String): Boolean
}
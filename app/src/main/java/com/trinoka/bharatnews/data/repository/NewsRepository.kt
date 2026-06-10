package com.trinoka.bharatnews.data.repository

import com.trinoka.bharatnews.data.api.RetrofitInstance
import com.trinoka.bharatnews.data.db.AppDatabase
import com.trinoka.bharatnews.data.model.BookmarkEntity

class NewsRepository(val db: AppDatabase) {

    suspend fun getTopHeadlines(category: String?, page: String?, language: String) =
        RetrofitInstance.api.getTopHeadlines(category = category, page = page, language = language)

    suspend fun searchNews(query: String, page: String?, language: String) =
        RetrofitInstance.api.searchNews(query = query, page = page, language = language)

    suspend fun getLatestNews(language: String) =
        RetrofitInstance.api.getLatestNews(language = language)

    // Bookmarks
    suspend fun upsert(article: BookmarkEntity) = db.getBookmarkDao().upsert(article)

    fun getAllBookmarks() = db.getBookmarkDao().getAllBookmarks()

    suspend fun deleteBookmark(article: BookmarkEntity) = db.getBookmarkDao().deleteBookmark(article)

    suspend fun isBookmarked(articleId: String) = db.getBookmarkDao().isBookmarked(articleId)
}
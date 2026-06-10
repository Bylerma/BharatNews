package com.trinoka.bharatnews.ui.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trinoka.bharatnews.data.model.BookmarkEntity
import com.trinoka.bharatnews.data.repository.NewsRepository
import kotlinx.coroutines.launch

class BookmarksViewModel(private val repository: NewsRepository) : ViewModel() {

    fun getSavedNews() = repository.getAllBookmarks()

    fun deleteArticle(article: BookmarkEntity) = viewModelScope.launch {
        repository.deleteBookmark(article)
    }

    fun saveArticle(article: BookmarkEntity) = viewModelScope.launch {
        repository.upsert(article)
    }
}
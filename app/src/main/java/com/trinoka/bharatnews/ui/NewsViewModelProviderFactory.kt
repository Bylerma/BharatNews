package com.trinoka.bharatnews.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trinoka.bharatnews.data.repository.NewsRepository
import com.trinoka.bharatnews.ui.bookmarks.BookmarksViewModel
import com.trinoka.bharatnews.ui.home.HomeViewModel
import com.trinoka.bharatnews.ui.search.SearchViewModel

class NewsViewModelProviderFactory(
    private val application: Application,
    private val newsRepository: NewsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(application, newsRepository) as T
        }
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(application, newsRepository) as T
        }
        if (modelClass.isAssignableFrom(BookmarksViewModel::class.java)) {
            return BookmarksViewModel(newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
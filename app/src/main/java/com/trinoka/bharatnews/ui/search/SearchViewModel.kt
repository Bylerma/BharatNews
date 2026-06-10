package com.trinoka.bharatnews.ui.search

import android.app.Application
import androidx.lifecycle.*
import com.trinoka.bharatnews.data.model.Article
import com.trinoka.bharatnews.data.model.NewsResponse
import com.trinoka.bharatnews.data.repository.NewsRepository
import com.trinoka.bharatnews.utils.PreferenceManager
import com.trinoka.bharatnews.utils.Resource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchViewModel(
    application: Application,
    private val repository: NewsRepository
) : AndroidViewModel(application) {

    private val preferenceManager = PreferenceManager(application)
    private val _searchNews = MutableLiveData<Resource<List<Article>>>()
    val searchNews: LiveData<Resource<List<Article>>> = _searchNews

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        _searchNews.postValue(Resource.Loading())
        try {
            val language = preferenceManager.languageFlow.first()
            val response = repository.searchNews(searchQuery, null, language)
            handleSearchNewsResponse(response)
        } catch (e: Exception) {
            _searchNews.postValue(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                _searchNews.postValue(Resource.Success(resultResponse.results))
            }
        } else {
            _searchNews.postValue(Resource.Error(response.message()))
        }
    }
}
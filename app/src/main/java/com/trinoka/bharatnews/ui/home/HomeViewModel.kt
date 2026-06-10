package com.trinoka.bharatnews.ui.home

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

class HomeViewModel(
    application: Application,
    private val repository: NewsRepository
) : AndroidViewModel(application) {

    private val preferenceManager = PreferenceManager(application)

    private val _articles = MutableLiveData<Resource<List<Article>>>()
    val articles: LiveData<Resource<List<Article>>> = _articles

    private val _category = MutableLiveData("top")
    val category: LiveData<String> = _category

    val isLoading = MutableLiveData<Boolean>(false)

    init {
        loadNews()
    }

    fun loadNews(category: String = "top") {
        _category.value = category
        viewModelScope.launch {
            _articles.postValue(Resource.Loading())
            isLoading.postValue(true)
            try {
                val language = preferenceManager.languageFlow.first()
                val response = repository.getTopHeadlines(category, null, language)
                handleNewsResponse(response)
            } catch (e: Exception) {
                _articles.postValue(Resource.Error(e.message ?: "An unknown error occurred"))
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    private fun handleNewsResponse(response: Response<NewsResponse>) {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                _articles.postValue(Resource.Success(resultResponse.results))
            }
        } else {
            _articles.postValue(Resource.Error(response.message()))
        }
    }
}
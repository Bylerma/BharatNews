package com.trinoka.bharatnews.data.api

import com.trinoka.bharatnews.data.model.NewsResponse
import com.trinoka.bharatnews.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    // Top headlines by category
    @GET("news")
    suspend fun getTopHeadlines(
        @Query("apikey") apiKey: String = Constants.API_KEY,
        @Query("country") country: String = Constants.COUNTRY,
        @Query("language") language: String = Constants.DEFAULT_LANGUAGE,
        @Query("category") category: String? = null,
        @Query("page") page: String? = null
    ): Response<NewsResponse>

    // Search news
    @GET("news")
    suspend fun searchNews(
        @Query("apikey") apiKey: String = Constants.API_KEY,
        @Query("q") query: String,
        @Query("country") country: String = Constants.COUNTRY,
        @Query("language") language: String = Constants.DEFAULT_LANGUAGE,
        @Query("page") page: String? = null
    ): Response<NewsResponse>

    // Latest news (no filters)
    @GET("latest")
    suspend fun getLatestNews(
        @Query("apikey") apiKey: String = Constants.API_KEY,
        @Query("country") country: String = Constants.COUNTRY,
        @Query("language") language: String = Constants.DEFAULT_LANGUAGE
    ): Response<NewsResponse>
}
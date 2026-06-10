package com.trinoka.bharatnews.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    @SerializedName("article_id")
    val articleId: String,
    val title: String,
    val link: String,
    val description: String?,
    val content: String?,
    val pubDate: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("source_name")
    val sourceName: String?,
    val category: List<String>?,
    val country: List<String>?,
    val language: String?
) : Parcelable
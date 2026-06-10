package com.trinoka.bharatnews.utils

import com.trinoka.bharatnews.data.model.Article
import com.trinoka.bharatnews.data.model.BookmarkEntity

fun BookmarkEntity.toArticle(): Article {
    return Article(
        articleId = this.article_id,
        title = this.title,
        link = this.link,
        description = this.description,
        content = null,
        pubDate = this.pubDate,
        imageUrl = this.image_url,
        sourceName = this.source_name,
        category = null,
        country = null,
        language = null
    )
}

fun Article.toBookmarkEntity(): BookmarkEntity {
    return BookmarkEntity(
        article_id = this.articleId,
        title = this.title,
        description = this.description,
        link = this.link,
        image_url = this.imageUrl,
        source_name = this.sourceName,
        pubDate = this.pubDate
    )
}
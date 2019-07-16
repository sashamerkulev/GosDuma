package ru.merkulyevsasha.database.mappers

import ru.merkulyevsasha.core.mappers.Mapper
import ru.merkulyevsasha.core.models.Article
import ru.merkulyevsasha.database.entities.ArticleEntity

class ArticleMapper : Mapper<Article, ArticleEntity> {
    override fun map(item: Article): ArticleEntity {
        return ArticleEntity(
            item.articleId,
            item.sourceName,
            item.title,
            item.link,
            item.description ?: "",
            item.pubDate,
            item.lastActivityDate,
            item.category,
            item.pictureUrl ?: "",
            item.usersLikeCount,
            item.usersDislikeCount,
            item.usersCommentCount,
            item.isUserLiked,
            item.isUserDisliked,
            item.isUserCommented,
            item.title.toLowerCase() + (item.description ?: "").toLowerCase()
        )
    }
}
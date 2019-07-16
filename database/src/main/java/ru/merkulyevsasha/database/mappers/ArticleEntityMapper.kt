package ru.merkulyevsasha.database.mappers

import ru.merkulyevsasha.core.mappers.Mapper
import ru.merkulyevsasha.core.models.Article
import ru.merkulyevsasha.database.entities.ArticleEntity

class ArticleEntityMapper : Mapper<ArticleEntity, Article> {
    override fun map(item: ArticleEntity): Article {
        return Article(
            item.articleId,
            item.sourceName,
            item.title,
            item.link,
            item.description,
            item.pubDate,
            item.lastActivityDate,
            item.category,
            item.pictureUrl,
            item.usersLikeCount,
            item.usersDislikeCount,
            item.usersCommentCount,
            item.isUserLiked,
            item.isUserDisliked,
            item.isUserCommented
        )
    }
}
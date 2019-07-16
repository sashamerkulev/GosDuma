package ru.merkulyevsasha.database.mappers

import ru.merkulyevsasha.core.mappers.Mapper
import ru.merkulyevsasha.core.models.ArticleComment
import ru.merkulyevsasha.database.BuildConfig
import ru.merkulyevsasha.database.entities.ArticleCommentEntity

class ArticleCommentEntityMapper(private val authorization: String) : Mapper<ArticleCommentEntity, ArticleComment> {
    override fun map(item: ArticleCommentEntity): ArticleComment {
        return ArticleComment(
            item.articleId,
            item.commentId,
            item.userId,
            item.userName,
            item.pubDate,
            item.lastActivityDate,
            item.comment,
            item.status,
            item.usersLikeCount,
            item.usersDislikeCount,
            item.isUserLiked,
            item.isUserDisliked,
            item.owner,
            BuildConfig.API_URL + "/users/${item.userId}/downloadPhoto",
            authorization
        )
    }
}
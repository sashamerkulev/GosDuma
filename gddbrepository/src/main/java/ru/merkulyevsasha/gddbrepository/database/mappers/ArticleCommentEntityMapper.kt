package ru.merkulyevsasha.gddbrepository.database.mappers

import ru.merkulyevsasha.core.mappers.Mapper
import ru.merkulyevsasha.core.models.ArticleComment
import ru.merkulyevsasha.gddatabase.entities.ArticleCommentEntity

class ArticleCommentEntityMapper(private val authorization: String, private val baseUrl: String) : Mapper<ArticleCommentEntity, ArticleComment> {
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
            baseUrl + "/users/${item.userId}/downloadPhoto",
            authorization
        )
    }
}
package ru.merkulyevsasha.database.mappers

import ru.merkulyevsasha.core.mappers.Mapper
import ru.merkulyevsasha.core.models.ArticleComment
import ru.merkulyevsasha.database.entities.ArticleCommentEntity

class ArticleCommentMapper : Mapper<ArticleComment, ArticleCommentEntity> {
    override fun map(item: ArticleComment): ArticleCommentEntity {
        return ArticleCommentEntity(
            item.articleId,
            item.commentId,
            item.userId,
            item.userName,
            item.pubDate,
            item.lastActivityDate,
            item.comment,
            item.statusId,
            item.usersLikeCount,
            item.usersDislikeCount,
            item.isUserLiked,
            item.isUserDisliked,
            item.owner
        )
    }
}
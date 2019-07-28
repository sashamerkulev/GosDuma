package ru.merkulyevsasha.data.database.mappers

import ru.merkulyevsasha.core.mappers.Mapper
import ru.merkulyevsasha.database.entities.AktCommentEntity
import ru.merkulyevsasha.gdcore.models.AktComment

class AktCommentMapper : Mapper<AktComment, AktCommentEntity> {
    override fun map(item: AktComment): AktCommentEntity {
        return AktCommentEntity(
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
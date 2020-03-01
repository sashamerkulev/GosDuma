package ru.merkulyevsasha.gddbrepository.database.mappers

import ru.merkulyevsasha.core.mappers.Mapper
import ru.merkulyevsasha.gdcore.models.AktComment
import ru.merkulyevsasha.gddatabase.entities.AktCommentEntity

class AktCommentEntityMapper(
    private val authorization: String,
    private val baseUrl: String
) : Mapper<AktCommentEntity, AktComment> {
    override fun map(item: AktCommentEntity): AktComment {
        return AktComment(
            item.aktId,
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
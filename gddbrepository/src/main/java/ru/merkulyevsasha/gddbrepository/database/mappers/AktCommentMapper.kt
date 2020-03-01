package ru.merkulyevsasha.gddbrepository.database.mappers

import ru.merkulyevsasha.core.mappers.Mapper
import ru.merkulyevsasha.gddatabase.entities.AktCommentEntity
import ru.merkulyevsasha.gdcore.models.AktComment

class AktCommentMapper : Mapper<AktComment, AktCommentEntity> {
    override fun map(item: AktComment): AktCommentEntity {
        return AktCommentEntity(
            item.aktId,
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
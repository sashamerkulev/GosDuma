package ru.merkulyevsasha.data.network.mappers

import ru.merkulyevsasha.core.mappers.Mapper
import ru.merkulyevsasha.gdcore.models.AktComment
import ru.merkulyevsasha.gdnetwork.models.AktCommentResponse

class AktCommentsMapper(private val authorization: String, private val baseUrl: String) : Mapper<AktCommentResponse, AktComment> {
    override fun map(item: AktCommentResponse): AktComment {
        return AktComment(
            item.articleId,
            item.commentId,
            item.userId,
            item.userName ?: "",
            item.pubDate,
            item.lastActivityDate,
            item.comment,
            item.status,
            item.likes,
            item.dislike,
            item.like > 0,
            item.dislike > 0,
            item.owner == 1,
            baseUrl + "/users/${item.userId}/downloadPhoto",
            authorization
        )
    }
}
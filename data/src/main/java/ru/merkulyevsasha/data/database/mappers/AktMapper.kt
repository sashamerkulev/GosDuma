package ru.merkulyevsasha.data.database.mappers

import ru.merkulyevsasha.core.mappers.Mapper
import ru.merkulyevsasha.database.entities.AktEntity
import ru.merkulyevsasha.gdcore.models.Akt

class AktMapper : Mapper<Akt, AktEntity> {
    override fun map(item: Akt): AktEntity {
        return AktEntity(
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
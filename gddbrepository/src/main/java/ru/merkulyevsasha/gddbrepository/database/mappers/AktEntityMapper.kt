package ru.merkulyevsasha.gddbrepository.database.mappers

import ru.merkulyevsasha.core.mappers.Mapper
import ru.merkulyevsasha.gddatabase.entities.AktEntity
import ru.merkulyevsasha.gdcore.models.Akt

class AktEntityMapper : Mapper<AktEntity, Akt> {
    override fun map(item: AktEntity): Akt {
        return Akt(
            item.aktId,
            item.sourceId,
            item.sourceId,
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
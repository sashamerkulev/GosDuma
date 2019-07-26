package ru.merkulyevsasha.data.network.mappers

import ru.merkulyevsasha.core.mappers.Mapper
import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdnetwork.models.AktResponse
import java.util.*

class AktMapper : Mapper<AktResponse, Akt> {
    override fun map(item: AktResponse): Akt {
        return Akt(
            item.articleId,
            item.sourceName,
            item.title,
            item.link,
            item.description,
            item.pubDate,
            item.lastActivityDate ?: Date(0),
            item.category,
            item.pictureUrl,
            item.likes,
            item.dislikes,
            item.comments,
            item.like > 0,
            item.dislike > 0,
            item.comment > 0
        )
    }
}
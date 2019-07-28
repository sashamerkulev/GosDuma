package ru.merkulyevsasha.gddomain.mappers

import ru.merkulyevsasha.core.mappers.Mapper
import ru.merkulyevsasha.gdcore.database.GDDatabaseRepository
import ru.merkulyevsasha.gdcore.models.Akt

class AktSourceNameMapper(private val databaseRepository: GDDatabaseRepository) : Mapper<Akt, Akt> {

    private var rssSourceNameMap = mutableMapOf<String, String>()

    override fun map(item: Akt): Akt {
        if (rssSourceNameMap.isEmpty()) {
            val map = databaseRepository.getRssSources().associateBy({ it.name }, { it.title })
            rssSourceNameMap.putAll(map)
        }
        return Akt(item.articleId,
            rssSourceNameMap[item.sourceName] ?: item.sourceName,
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

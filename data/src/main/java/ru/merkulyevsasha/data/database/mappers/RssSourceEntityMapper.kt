package ru.merkulyevsasha.data.database.mappers

import ru.merkulyevsasha.core.mappers.Mapper
import ru.merkulyevsasha.core.models.RssSource
import ru.merkulyevsasha.database.entities.RssSourceEntity

class RssSourceEntityMapper : Mapper<RssSourceEntity, RssSource> {
    override fun map(item: RssSourceEntity): RssSource {
        return RssSource(item.name, item.title)
    }
}
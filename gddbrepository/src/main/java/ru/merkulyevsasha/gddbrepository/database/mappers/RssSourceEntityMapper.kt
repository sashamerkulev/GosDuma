package ru.merkulyevsasha.gddbrepository.database.mappers

import ru.merkulyevsasha.core.mappers.Mapper
import ru.merkulyevsasha.core.models.RssSource
import ru.merkulyevsasha.gddatabase.entities.RssSourceEntity

class RssSourceEntityMapper : Mapper<RssSourceEntity, RssSource> {
    override fun map(item: RssSourceEntity): RssSource {
        return RssSource(item.sourceId, item.sourceName, item.checked)
    }
}
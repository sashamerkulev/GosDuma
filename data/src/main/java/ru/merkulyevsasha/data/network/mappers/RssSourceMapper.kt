package ru.merkulyevsasha.data.network.mappers

import ru.merkulyevsasha.core.mappers.Mapper
import ru.merkulyevsasha.core.models.RssSource
import ru.merkulyevsasha.network.models.RssSourceResponse

class RssSourceMapper : Mapper<RssSourceResponse, RssSource> {
    override fun map(item: RssSourceResponse): RssSource {
        return RssSource(item.name, item.title)
    }
}
package ru.merkulyevsasha.gddatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.merkulyevsasha.gddatabase.entities.RssSourceEntity

@Dao
interface SetupDao {
    @Insert
    fun saveRssSources(items: List<RssSourceEntity>)

    @Query("update sources set checked = :checked where sourceId = :sourceId")
    fun updateRssSource(checked: Boolean, sourceId: String)

    @Query("delete from sources")
    fun deleteRssSources()

    @Query("select sourceId, sourceName, checked from sources")
    fun getRssSources(): List<RssSourceEntity>
}

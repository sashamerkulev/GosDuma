package ru.merkulyevsasha.gddatabase.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sources")
data class RssSourceEntity(
    @PrimaryKey
    val sourceId: String,
    val sourceName: String,
    val checked: Boolean
)
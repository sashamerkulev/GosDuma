package ru.merkulyevsasha.data.database

import android.content.Context
import androidx.room.Room
import ru.merkulyevsasha.database.data.GosdumaRoomDatabase
import ru.merkulyevsasha.gddata.database.GosdumaDatabaseSource
import ru.merkulyevsasha.gddata.database.GosdumaDatabaseSourceImpl

object GosdumaRoomDatabaseSourceCreator {
    fun create(context: Context, dbName: String): GosdumaDatabaseSource {
        return GosdumaDatabaseSourceImpl(Room
            .databaseBuilder(context, GosdumaRoomDatabase::class.java, dbName)
            .fallbackToDestructiveMigration()
            .build())
    }
}
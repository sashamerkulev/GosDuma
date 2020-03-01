package ru.merkulyevsasha.gddbrepository.database

import android.content.Context
import androidx.room.Room
import ru.merkulyevsasha.gddatabase.data.GdRoomDatabase

object GosdumaRoomDatabaseSourceCreator {
    fun create(context: Context, dbName: String): GdDatabaseSource {
        return GdDatabaseSourceImpl(Room
            .databaseBuilder(context, GdRoomDatabase::class.java, dbName)
            .fallbackToDestructiveMigration()
            .build())
    }
}
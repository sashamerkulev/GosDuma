package ru.merkulyevsasha.data.database

import android.content.Context
import androidx.room.Room
import ru.merkulyevsasha.database.data.NewsRoomDatabase

object NewsRoomDatabaseSourceCreator {
    fun create(context: Context, dbName: String): NewsDatabaseSource {
        return NewsDatabaseSourceImpl(Room
            .databaseBuilder(context, NewsRoomDatabase::class.java, dbName)
            .fallbackToDestructiveMigration()
            .build())
    }
}
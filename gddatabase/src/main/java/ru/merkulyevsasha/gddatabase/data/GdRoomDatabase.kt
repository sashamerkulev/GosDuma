package ru.merkulyevsasha.gddatabase.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.merkulyevsasha.database.dao.ArticleCommentsDao
import ru.merkulyevsasha.database.dao.ArticleDao
import ru.merkulyevsasha.database.dao.SetupDao
import ru.merkulyevsasha.database.data.RoomTypeConverters
import ru.merkulyevsasha.database.entities.AktCommentEntity
import ru.merkulyevsasha.database.entities.AktEntity
import ru.merkulyevsasha.database.entities.ArticleCommentEntity
import ru.merkulyevsasha.database.entities.ArticleEntity
import ru.merkulyevsasha.database.entities.RssSourceEntity
import ru.merkulyevsasha.gddatabase.dao.AktCommentsDao
import ru.merkulyevsasha.gddatabase.dao.AktDao

@Database(entities = [ArticleEntity::class, ArticleCommentEntity::class, RssSourceEntity::class,
    AktEntity::class, AktCommentEntity::class], version = 1, exportSchema = false)
@TypeConverters(RoomTypeConverters::class)
abstract class GdRoomDatabase : RoomDatabase() {
    abstract val articleDao: ArticleDao
    abstract val articleCommentsDao: ArticleCommentsDao
    abstract val aktDao: AktDao
    abstract val aktCommentsDao: AktCommentsDao
    abstract val setupDao: SetupDao
}
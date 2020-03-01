package ru.merkulyevsasha.gddatabase.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.merkulyevsasha.gddatabase.dao.AktCommentsDao
import ru.merkulyevsasha.gddatabase.dao.AktDao
import ru.merkulyevsasha.gddatabase.dao.ArticleCommentsDao
import ru.merkulyevsasha.gddatabase.dao.ArticleDao
import ru.merkulyevsasha.gddatabase.dao.SetupDao
import ru.merkulyevsasha.gddatabase.entities.AktCommentEntity
import ru.merkulyevsasha.gddatabase.entities.AktEntity
import ru.merkulyevsasha.gddatabase.entities.ArticleCommentEntity
import ru.merkulyevsasha.gddatabase.entities.ArticleEntity
import ru.merkulyevsasha.gddatabase.entities.RssSourceEntity

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
package ru.merkulyevsasha.database.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.merkulyevsasha.database.dao.AktCommentsDao
import ru.merkulyevsasha.database.dao.AktDao
import ru.merkulyevsasha.database.dao.ArticleCommentsDao
import ru.merkulyevsasha.database.dao.ArticleDao
import ru.merkulyevsasha.database.dao.SetupDao
import ru.merkulyevsasha.database.entities.AktCommentEntity
import ru.merkulyevsasha.database.entities.AktEntity
import ru.merkulyevsasha.database.entities.ArticleCommentEntity
import ru.merkulyevsasha.database.entities.ArticleEntity
import ru.merkulyevsasha.database.entities.RssSourceEntity

@Database(entities = [ArticleEntity::class, ArticleCommentEntity::class, RssSourceEntity::class,
    AktEntity::class, AktCommentEntity::class
], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class GosdumaRoomDatabase : RoomDatabase() {
    abstract val articleDao: ArticleDao
    abstract val articleCommentsDao: ArticleCommentsDao
    abstract val setupDao: SetupDao
    abstract val aktDao: AktDao
    abstract val aktCommentsDao: AktCommentsDao
}

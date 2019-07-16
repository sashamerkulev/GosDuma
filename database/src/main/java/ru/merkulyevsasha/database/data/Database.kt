package ru.merkulyevsasha.database.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.merkulyevsasha.database.dao.ArticleCommentsDao
import ru.merkulyevsasha.database.dao.ArticleDao
import ru.merkulyevsasha.database.dao.SetupDao
import ru.merkulyevsasha.database.entities.ArticleCommentEntity
import ru.merkulyevsasha.database.entities.ArticleEntity
import ru.merkulyevsasha.database.entities.RssSourceEntity

@Database(entities = [ArticleEntity::class, ArticleCommentEntity::class, RssSourceEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract val articleDao: ArticleDao
    abstract val articleCommentsDao: ArticleCommentsDao
    abstract val setupDao: SetupDao
}

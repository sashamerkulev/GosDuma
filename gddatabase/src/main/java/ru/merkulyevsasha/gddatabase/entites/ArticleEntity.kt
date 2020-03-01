package ru.merkulyevsasha.gddatabase.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "articles", indices = [Index("sourceId"), Index("search"),
    Index("pubDate"), Index("lastActivityDate")])
class ArticleEntity(
    @PrimaryKey
    val articleId: Int = 0,
    val sourceId: String,
    val title: String,
    val link: String,
    val description: String,
    val pubDate: Date,
    val lastActivityDate: Date,
    val category: String,
    val pictureUrl: String,
    val usersLikeCount: Int,
    val usersDislikeCount: Int,
    val usersCommentCount: Int,
    val isUserLiked: Boolean,
    val isUserDisliked: Boolean,
    val isUserCommented: Boolean,
    val search: String
)
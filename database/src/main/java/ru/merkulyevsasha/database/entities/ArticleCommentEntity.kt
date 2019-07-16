package ru.merkulyevsasha.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "comments",
    foreignKeys = [ForeignKey(entity = ArticleEntity::class,
        parentColumns = ["articleId"], childColumns = ["articleId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("articleId"), Index("pubDate")])
class ArticleCommentEntity(
    val articleId: Int,
    @PrimaryKey
    val commentId: Int,
    val userId: Int,
    val userName: String,
    val pubDate: Date,
    val lastActivityDate: Date,
    val comment: String,
    val status: Int,
    val usersLikeCount: Int,
    val usersDislikeCount: Int,
    val isUserLiked: Boolean,
    val isUserDisliked: Boolean,
    val owner: Boolean
)
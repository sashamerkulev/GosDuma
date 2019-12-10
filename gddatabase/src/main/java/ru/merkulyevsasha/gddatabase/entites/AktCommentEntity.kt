package ru.merkulyevsasha.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "aktComments",
    foreignKeys = [ForeignKey(entity = AktEntity::class,
        parentColumns = ["aktId"], childColumns = ["aktId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("aktId"), Index("pubDate"), Index("lastActivityDate")])
class AktCommentEntity(
    val aktId: Int,
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
package ru.merkulyevsasha.gdcore.models

import ru.merkulyevsasha.core.models.ArticleOrComment
import java.util.*

data class AktComment(
    val aktId: Int,
    val commentId: Int,
    val userId: Int,
    val userName: String,
    val pubDate: Date,
    val lastActivityDate: Date,
    val comment: String,
    val statusId: Int,
    var usersLikeCount: Int,
    var usersDislikeCount: Int,
    var isUserLiked: Boolean,
    var isUserDisliked: Boolean,
    val owner: Boolean,
    val avatarUrl: String,
    val authorization: String
) : ArticleOrComment

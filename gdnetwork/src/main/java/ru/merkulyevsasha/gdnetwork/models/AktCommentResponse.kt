package ru.merkulyevsasha.gdnetwork.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class AktCommentResponse(
    @field:SerializedName("ArticleId") val articleId: Int,
    @field:SerializedName("CommentId") val commentId: Int,
    @field:SerializedName("UserId") val userId: Int,
    @field:SerializedName("Name") val userName: String?,
    @field:SerializedName("PubDate") val pubDate: Date,
    @field:SerializedName("LastActivityDate") val lastActivityDate: Date,
    @field:SerializedName("Comment") val comment: String,
    @field:SerializedName("Likes") var likes: Int,
    @field:SerializedName("Status") var status: Int,
    @field:SerializedName("Dislikes") var dislikes: Int,
    @field:SerializedName("Like") var like: Int,
    @field:SerializedName("Dislike") var dislike: Int,
    @field:SerializedName("Owner") var owner: Int
)

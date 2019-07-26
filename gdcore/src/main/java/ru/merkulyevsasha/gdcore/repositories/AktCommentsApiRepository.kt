package ru.merkulyevsasha.gdcore.repositories

import io.reactivex.Completable
import io.reactivex.Single
import ru.merkulyevsasha.gdcore.models.AktComment
import java.util.*

interface AktCommentsApiRepository {
    fun getAktComments(articleId: Int, lastArticleCommentsReadDate: Date): Single<List<AktComment>>
    fun addAktComment(articleId: Int, comment: String): Single<AktComment>
    fun likeAktComment(commentId: Int): Single<AktComment>
    fun dislikeAktComment(commentId: Int): Single<AktComment>
    fun deleteAktComment(commentId: Int): Completable
}
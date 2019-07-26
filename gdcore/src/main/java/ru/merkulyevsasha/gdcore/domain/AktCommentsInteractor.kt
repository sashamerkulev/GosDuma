package ru.merkulyevsasha.gdcore.domain

import io.reactivex.Completable
import io.reactivex.Single
import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdcore.models.AktComment

interface AktCommentsInteractor {
    fun getArticleComments(articleId: Int): Single<Pair<Akt, List<AktComment>>>
    fun refreshAndGetArticleComments(articleId: Int): Single<Pair<Akt, List<AktComment>>>
    fun addArticleComment(articleId: Int, comment: String): Single<AktComment>
    fun likeArticleComment(commentId: Int): Single<AktComment>
    fun dislikeArticleComment(commentId: Int): Single<AktComment>
    fun deleteArticleComment(commentId: Int): Completable
}

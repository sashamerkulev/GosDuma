package ru.merkulyevsasha.gdcore.domain

import io.reactivex.Completable
import io.reactivex.Single
import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdcore.models.AktComment

interface AktCommentsInteractor {
    fun getAktComments(articleId: Int): Single<Pair<Akt, List<AktComment>>>
    fun refreshAndGetAktComments(articleId: Int): Single<Pair<Akt, List<AktComment>>>
    fun addAktComment(articleId: Int, comment: String): Single<AktComment>
    fun likeAktComment(commentId: Int): Single<AktComment>
    fun dislikeAktComment(commentId: Int): Single<AktComment>
    fun deleteAktComment(commentId: Int): Completable
}

package ru.merkulyevsasha.data.database

import io.reactivex.Single
import ru.merkulyevsasha.database.entities.AktCommentEntity
import ru.merkulyevsasha.database.entities.AktEntity

interface GosdumaDatabaseSource : NewsDatabaseSource {
    fun getAkts(): Single<List<AktEntity>>
    fun searchAkts(searchText: String): Single<List<AktEntity>>
    fun getAktComments(articleId: Int): Single<List<AktCommentEntity>>
    fun addOrUpdateAkts(articles: List<AktEntity>)
    fun updateAkt(article: AktEntity)
    fun addOrUpdateAktComments(comments: List<AktCommentEntity>)
    fun updateAktComment(comment: AktCommentEntity, commentsCount: Int)
    fun getAkt(articleId: Int): Single<AktEntity>
}
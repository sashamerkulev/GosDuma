package ru.merkulyevsasha.gddbrepository.database

import io.reactivex.Single
import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gddatabase.entities.AktCommentEntity
import ru.merkulyevsasha.gddatabase.entities.AktEntity

interface GdDatabaseSource: NewsDatabaseSource {
    fun getAkts(): Single<List<Akt>>
    fun searchAkts(searchText: String): Single<List<Akt>>
    fun getAktComments(articleId: Int): Single<List<AktCommentEntity>>
    fun addOrUpdateAkts(articles: List<AktEntity>)
    fun updateAkt(article: AktEntity)
    fun addOrUpdateAktComments(comments: List<AktCommentEntity>)
    fun updateAktComment(comment: AktCommentEntity, commentsCount: Int)
    fun getAkt(articleId: Int): Single<Akt>
}
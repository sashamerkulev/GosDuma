package ru.merkulyevsasha.gdcore.domain

import io.reactivex.Single
import ru.merkulyevsasha.gdcore.models.Akt

interface AktInteractor {
    fun refreshAndGetArticles(): Single<List<Akt>>
    fun getArticles(): Single<List<Akt>>
    fun getUserActivityArticles(): Single<List<Akt>>
    fun likeArticle(articleId: Int): Single<Akt>
    fun dislikeArticle(articleId: Int): Single<Akt>
    fun getArticle(articleId: Int): Single<Akt>
    fun searchArticles(searchText: String?, byUserActivities: Boolean): Single<List<Akt>>
}

package ru.merkulyevsasha.gdcore.domain

import io.reactivex.Single
import ru.merkulyevsasha.gdcore.models.Akt

interface AktsInteractor {
    fun refreshAndGetAkts(): Single<List<Akt>>
    fun getAkts(): Single<List<Akt>>
    fun likeAkt(articleId: Int): Single<Akt>
    fun dislikeAkt(articleId: Int): Single<Akt>
    fun getAkt(articleId: Int): Single<Akt>
    fun searchAkts(searchText: String?, byUserActivities: Boolean): Single<List<Akt>>
}

package ru.merkulyevsasha.gdcore.repositories

import io.reactivex.Single
import ru.merkulyevsasha.gdcore.models.Akt
import java.util.*

interface AktsApiRepository {
    fun getAkts(lastArticleReadDate: Date): Single<List<Akt>>
    fun likeAkt(articleId: Int): Single<Akt>
    fun dislikeAkt(articleId: Int): Single<Akt>
    fun getAkt(articleId: Int): Single<Akt>
}
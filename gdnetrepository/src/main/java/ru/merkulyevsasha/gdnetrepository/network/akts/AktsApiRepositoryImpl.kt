package ru.merkulyevsasha.gdnetrepository.network.akts

import io.reactivex.Single
import ru.merkulyevsasha.core.preferences.KeyValueStorage
import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdcore.repositories.AktsApiRepository
import ru.merkulyevsasha.netrepository.network.base.BaseApiRepository
import java.util.*

class AktsApiRepositoryImpl(
    sharedPreferences: KeyValueStorage,
    baseUrl: String,
    debugMode: Boolean
) : BaseApiRepository(sharedPreferences, baseUrl, debugMode), AktsApiRepository {
    override fun getAkts(lastArticleReadDate: Date): Single<List<Akt>> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun likeAkt(articleId: Int): Single<Akt> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun dislikeAkt(articleId: Int): Single<Akt> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getAkt(articleId: Int): Single<Akt> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

}
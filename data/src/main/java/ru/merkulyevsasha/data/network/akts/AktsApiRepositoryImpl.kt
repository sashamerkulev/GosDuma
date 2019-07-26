package ru.merkulyevsasha.data.network.akts

import io.reactivex.Single
import ru.merkulyevsasha.core.preferences.KeyValueStorage
import ru.merkulyevsasha.data.network.base.BaseApiRepository
import ru.merkulyevsasha.data.network.mappers.AktMapper
import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdcore.repositories.AktsApiRepository
import ru.merkulyevsasha.gdnetwork.data.AktsApi
import java.text.SimpleDateFormat
import java.util.*

class AktsApiRepositoryImpl(
    sharedPreferences: KeyValueStorage,
    baseUrl: String,
    debugMode: Boolean
) : BaseApiRepository(sharedPreferences, baseUrl, debugMode), AktsApiRepository {

    private val aktMapper = AktMapper()

    private val api: AktsApi = retrofit.create(AktsApi::class.java)

    private val format = "yyyy-MM-dd'T'HH:mm:ss"
    private val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())

    override fun getAkts(lastArticleReadDate: Date): Single<List<Akt>> {
        return api.getAkts(simpleDateFormat.format(lastArticleReadDate))
            .flattenAsFlowable { it }
            .map { aktMapper.map(it) }
            .toList()
    }

    override fun likeAkt(articleId: Int): Single<Akt> {
        return api.likeAkt(articleId)
            .map { aktMapper.map(it) }
    }

    override fun dislikeAkt(articleId: Int): Single<Akt> {
        return api.dislikeAkt(articleId)
            .map { aktMapper.map(it) }
    }

    override fun getAkt(articleId: Int): Single<Akt> {
        return api.getAkt(articleId)
            .map { aktMapper.map(it) }
    }
}
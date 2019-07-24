package ru.merkulyevsasha.data.network.setup

import io.reactivex.Completable
import io.reactivex.Single
import ru.merkulyevsasha.core.models.RssSource
import ru.merkulyevsasha.core.models.Token
import ru.merkulyevsasha.core.preferences.KeyValueStorage
import ru.merkulyevsasha.core.repositories.SetupApiRepository
import ru.merkulyevsasha.data.network.base.BaseApiRepository
import ru.merkulyevsasha.data.network.mappers.RssSourceMapper
import ru.merkulyevsasha.network.data.SetupApi

class SetupApiRepositoryImpl(
    sharedPreferences: KeyValueStorage,
    baseUrl: String,
    debugMode: Boolean
) : BaseApiRepository(sharedPreferences, baseUrl, debugMode), SetupApiRepository {

    private val api: SetupApi = retrofit.create(SetupApi::class.java)

    private val rssSourceMapper = RssSourceMapper()

    override fun registerSetup(setupId: String): Single<Token> {
        return api.registerSetup(setupId, "")
            .map { Token(it.token) }
    }

    override fun updateFirebaseToken(firebaseId: String): Completable {
        return api.updateFirebaseToken(firebaseId)
    }

    override fun getRssSources(): Single<List<RssSource>> {
        return api.getRssSources()
            .flattenAsFlowable { it }
            .map { rssSourceMapper.map(it) }
            .toList()
    }
}
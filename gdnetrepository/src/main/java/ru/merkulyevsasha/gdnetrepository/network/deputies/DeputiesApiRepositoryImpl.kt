package ru.merkulyevsasha.gdnetrepository.network.deputies

import ru.merkulyevsasha.core.preferences.KeyValueStorage
import ru.merkulyevsasha.gdcore.models.Deputy
import ru.merkulyevsasha.gdcore.models.DeputyPages
import ru.merkulyevsasha.gdcore.repositories.DeputiesApiRepository
import ru.merkulyevsasha.gdnetrepository.network.base.CorBaseApiRepository
import ru.merkulyevsasha.gdnetwork.data.DeputiesApi

class DeputiesApiRepositoryImpl(
    private val sharedPreferences: KeyValueStorage,
    private val baseUrl: String,
    debugMode: Boolean
) : CorBaseApiRepository(sharedPreferences, baseUrl, debugMode), DeputiesApiRepository {

    private val api: DeputiesApi = retrofit.create(DeputiesApi::class.java)

    override suspend fun getDeputies(searchText: String, page: Int, pageSize: Int, orderFields: String, orderDirection: String): DeputyPages {
        val result = api.getDeputies(searchText, page, pageSize, orderFields, orderDirection)
        if (result.isSuccessful) {
            val resp = result.body()!!
            val token = "bearer " + sharedPreferences.getAccessToken()
            return DeputyPages(resp.page, resp.pageSize,
                resp.pageNumbers, resp.rowNumbers, resp.deputies.map {
                val url = baseUrl + "/deputies/" + it.id + "/image"
                Deputy(it.id, it.name, it.isCurrent, it.position, url, token)
            }
            )
        } else {
            val err = result.errorBody()
            throw Exception(err?.string())
        }
    }

}
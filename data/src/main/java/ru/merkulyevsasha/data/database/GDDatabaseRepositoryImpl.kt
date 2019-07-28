package ru.merkulyevsasha.data.database

import io.reactivex.Single
import ru.merkulyevsasha.core.preferences.KeyValueStorage
import ru.merkulyevsasha.data.database.mappers.AktCommentEntityMapper
import ru.merkulyevsasha.data.database.mappers.AktCommentMapper
import ru.merkulyevsasha.data.database.mappers.AktEntityMapper
import ru.merkulyevsasha.data.database.mappers.AktMapper
import ru.merkulyevsasha.gdcore.database.GDDatabaseRepository
import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdcore.models.AktComment

class GDDatabaseRepositoryImpl(
    private val gosdumaDatabaseSource: GosdumaDatabaseSource,
    keyValueStorage: KeyValueStorage,
    baseUrl: String
) : DatabaseRepositoryImpl(gosdumaDatabaseSource, keyValueStorage, baseUrl), GDDatabaseRepository {

    private val aktEntityMapper = AktEntityMapper()
    private val aktMapper = AktMapper()
    private val aktCommentEntityMapper by lazy { AktCommentEntityMapper("bearer " + keyValueStorage.getAccessToken(), baseUrl) }
    private val aktCommentMapper = AktCommentMapper()


    override fun getAkt(aktId: Int): Single<Akt> {
        return gosdumaDatabaseSource.getAkt(aktId)
            .map { aktEntityMapper.map(it) }
    }

    override fun getAkts(): Single<List<Akt>> {
        return gosdumaDatabaseSource.getAkts()
            .flattenAsFlowable { it }
            .map { aktEntityMapper.map(it) }
            .toList()
    }

    override fun getAktComments(aktId: Int): Single<List<AktComment>> {
        return gosdumaDatabaseSource.getAktComments(aktId)
            .flattenAsFlowable { it }
            .map { aktCommentEntityMapper.map(it) }
            .toList()
    }

    override fun addOrUpdateAkts(akts: List<Akt>) {
        gosdumaDatabaseSource.addOrUpdateAkts(akts.map { aktMapper.map(it) })
    }

    override fun updateAkt(akt: Akt) {
        gosdumaDatabaseSource.updateAkt(aktMapper.map(akt))
    }

    override fun searchAkts(searchText: String): Single<List<Akt>> {
        return gosdumaDatabaseSource.searchAkts("%${searchText.toLowerCase()}%")
            .flattenAsFlowable { it }
            .map { aktEntityMapper.map(it) }
            .toList()
    }

    override fun addOrUpdateAktComments(comments: List<AktComment>) {
        gosdumaDatabaseSource.addOrUpdateAktComments(comments.map { aktCommentMapper.map(it) })
    }

    override fun updateAktComment(comment: AktComment, commentsCount: Int) {
        gosdumaDatabaseSource.updateAktComment(aktCommentMapper.map(comment), commentsCount)
    }
}
package ru.merkulyevsasha.gdcore.database

import io.reactivex.Single
import ru.merkulyevsasha.core.repositories.NewsDatabaseRepository
import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdcore.models.AktComment

interface GdDatabaseRepository : NewsDatabaseRepository {

    fun getAkt(aktId: Int): Single<Akt>
    fun getAkts(): Single<List<Akt>>
    fun getAktComments(aktId: Int): Single<List<AktComment>>
    fun addOrUpdateAkts(akts: List<Akt>)
    fun updateAkt(akt: Akt)
    fun searchAkts(searchText: String): Single<List<Akt>>
    fun addOrUpdateAktComments(comments: List<AktComment>)
    fun updateAktComment(comment: AktComment, commentsCount: Int)

}

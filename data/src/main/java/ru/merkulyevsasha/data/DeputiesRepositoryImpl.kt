package ru.merkulyevsasha.data

import io.reactivex.Single
import ru.merkulyevsasha.gdcore.database.GDDatabaseRepository
import ru.merkulyevsasha.gdcore.models.Deputy
import ru.merkulyevsasha.gdcore.repositories.DeputiesRepository

class DeputiesRepositoryImpl(private val db: GDDatabaseRepository) : DeputiesRepository {
    override fun getDeputies2(search: String, order: String, position: String, isCurrent: Int): Single<List<Deputy>> {
        return Single.fromCallable { db.getDeputies(search, order, position, isCurrent) }
    }
}

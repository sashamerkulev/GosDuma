package ru.merkulyevsasha.gdcore.database

import ru.merkulyevsasha.gdcore.models.Article
import ru.merkulyevsasha.gdcore.models.Codifier
import ru.merkulyevsasha.gdcore.models.Deputy
import ru.merkulyevsasha.gdcore.models.DeputyRequest
import ru.merkulyevsasha.gdcore.models.Law
import ru.merkulyevsasha.gdcore.models.ListData

interface GDDatabaseRepository {
    fun selectAll(tableName: String): List<ListData>

    fun deleteAll(tableName: String)

    fun deleteArticles(source: Int)

    fun addArticles(source: Int, articles: List<Article>)

    fun getArticles(source: Int): List<Article>

    fun getDeputies(searchText: String, orderBy: String, position: String, isCurrent: Int): List<Deputy>

    fun getDeputyLaws(deputyId: Int, searchText: String, orderBy: String): List<Law>

    fun getLaws(searchText: String, orderBy: String): List<Law>

    fun getDeputyRequests(searchText: String, orderBy: String): List<DeputyRequest>

    fun getPhaseById(id: Int): Codifier

    fun getStageById(id: Int): Codifier

    fun getProfileComittees(id: Int): List<Codifier>

    fun getCoexecutorCommittees(id: Int): List<Codifier>

    fun getLawDeputies(id: Int): List<Codifier>

    fun getLawRegionals(id: Int): List<Codifier>

    fun getLawFederals(id: Int): List<Codifier>
}

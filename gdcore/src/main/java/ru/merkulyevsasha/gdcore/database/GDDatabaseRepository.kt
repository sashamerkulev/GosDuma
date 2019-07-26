package ru.merkulyevsasha.gdcore.database

import ru.merkulyevsasha.core.models.Article
import ru.merkulyevsasha.gdcore.models.Deputy
import ru.merkulyevsasha.gdcore.models.DeputyRequest
import ru.merkulyevsasha.gdcore.models.Law

interface GDDatabaseRepository {

    fun deleteAll(tableName: String)

    fun deleteArticles(source: Int)

    fun addArticles(source: Int, articles: List<Article>)

    fun getArticles(source: Int): List<Article>

    fun getDeputies(searchText: String, orderBy: String, position: String, isCurrent: Int): List<Deputy>

    fun getDeputyLaws(deputyId: Int, searchText: String, orderBy: String): List<Law>

    fun getLaws(searchText: String, orderBy: String): List<Law>

    fun getDeputyRequests(searchText: String, orderBy: String): List<DeputyRequest>
}

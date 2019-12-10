package ru.merkulyevsasha.gddata.database

import io.reactivex.Single
import ru.merkulyevsasha.core.models.Article
import ru.merkulyevsasha.core.models.ArticleComment
import ru.merkulyevsasha.core.models.RssSource
import ru.merkulyevsasha.core.preferences.KeyValueStorage
import ru.merkulyevsasha.data.database.mappers.ArticleCommentEntityMapper
import ru.merkulyevsasha.data.database.mappers.ArticleCommentMapper
import ru.merkulyevsasha.data.database.mappers.ArticleMapper
import ru.merkulyevsasha.data.database.mappers.RssSourceEntityMapper
import ru.merkulyevsasha.data.database.mappers.RssSourceMapper
import ru.merkulyevsasha.gdcore.database.GdDatabaseRepository
import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdcore.models.AktComment
import ru.merkulyevsasha.gddata.database.mappers.AktCommentEntityMapper
import ru.merkulyevsasha.gddata.database.mappers.AktCommentMapper
import ru.merkulyevsasha.gddata.database.mappers.AktMapper
import java.util.*

class GdDatabaseRepositoryImpl(
    private val gdDatabaseSource: GdDatabaseSource,
    keyValueStorage: KeyValueStorage,
    baseUrl: String
) : GdDatabaseRepository {

    private val articleMapper = ArticleMapper()
    private val articleCommentEntityMapper by lazy { ArticleCommentEntityMapper("bearer " + keyValueStorage.getAccessToken(), baseUrl) }
    private val articleCommentMapper = ArticleCommentMapper()
    private val rssSourceMapper = RssSourceMapper()
    private val rssSourceEntityMapper = RssSourceEntityMapper()
    private val aktMapper = AktMapper()
    private val aktCommentEntityMapper by lazy { AktCommentEntityMapper("bearer " + keyValueStorage.getAccessToken(), baseUrl) }
    private val aktCommentMapper = AktCommentMapper()

    override fun getAkt(aktId: Int): Single<Akt> {
        return gdDatabaseSource.getAkt(aktId)
    }

    override fun getAkts(): Single<List<Akt>> {
        return gdDatabaseSource.getAkts()
    }

    override fun getAktComments(aktId: Int): Single<List<AktComment>> {
        return gdDatabaseSource.getAktComments(aktId)
            .flattenAsFlowable { it }
            .map { aktCommentEntityMapper.map(it) }
            .toList()
    }

    override fun addOrUpdateAkts(akts: List<Akt>) {
        gdDatabaseSource.addOrUpdateAkts(akts.map { aktMapper.map(it) })
    }

    override fun updateAkt(akt: Akt) {
        gdDatabaseSource.updateAkt(aktMapper.map(akt))
    }

    override fun searchAkts(searchText: String): Single<List<Akt>> {
        return gdDatabaseSource.searchAkts("%${searchText.toLowerCase(Locale.getDefault())}%")
    }

    override fun addOrUpdateAktComments(comments: List<AktComment>) {
        gdDatabaseSource.addOrUpdateAktComments(comments.map { aktCommentMapper.map(it) })
    }

    override fun updateAktComment(comment: AktComment, commentsCount: Int) {
        gdDatabaseSource.updateAktComment(aktCommentMapper.map(comment), commentsCount)
    }

    override fun getArticle(articleId: Int): Single<Article> {
        return gdDatabaseSource.getArticle(articleId)
    }

    override fun getArticles(): Single<List<Article>> {
        return gdDatabaseSource.getArticles()
    }

    override fun getArticleComments(articleId: Int): Single<List<ArticleComment>> {
        return gdDatabaseSource.getArticleComments(articleId)
            .flattenAsFlowable { it }
            .map { articleCommentEntityMapper.map(it) }
            .toList()
    }

    override fun getUserActivityArticles(): Single<List<Article>> {
        return gdDatabaseSource.getUserActivityArticles()
    }

    override fun getSourceArticles(sourceName: String): Single<List<Article>> {
        return gdDatabaseSource.getSourceArticles(sourceName)
    }

    override fun searchArticles(searchText: String, byUserActivities: Boolean): Single<List<Article>> {
        return Single.fromCallable { byUserActivities }
            .flatMap { ua ->
                if (ua) gdDatabaseSource.searchUserActivitiesArticles("%${searchText.toLowerCase(Locale.getDefault())}%")
                else gdDatabaseSource.searchArticles("%${searchText.toLowerCase(Locale.getDefault())}%")
            }
    }

    override fun searchSourceArticles(sourceName: String, searchText: String): Single<List<Article>> {
        return gdDatabaseSource.searchSourceArticles(sourceName, "%${searchText.toLowerCase(Locale.getDefault())}%")
    }

    override fun removeOldNotUserActivityArticles(cleanDate: Date) {
        gdDatabaseSource.removeOldNotUserActivityArticles(cleanDate)
    }

    override fun removeOldUserActivityArticles(cleanDate: Date) {
        gdDatabaseSource.removeOldUserActivityArticles(cleanDate)
    }

    override fun deleteRssSources() {
        gdDatabaseSource.deleteRssSources()
    }

    override fun saveRssSources(sources: List<RssSource>) {
        gdDatabaseSource.saveRssSources(sources.map { rssSourceMapper.map(it) })
    }

    override fun getRssSources(): List<RssSource> {
        return gdDatabaseSource.getRssSources()
            .map { rssSourceEntityMapper.map(it) }
    }

    override fun addOrUpdateArticles(articles: List<Article>) {
        gdDatabaseSource.addOrUpdateArticles(articles.map { articleMapper.map(it) })
    }

    override fun updateArticle(article: Article) {
        gdDatabaseSource.updateArticle(articleMapper.map(article))
    }

    override fun addOrUpdateArticleComments(comments: List<ArticleComment>) {
        gdDatabaseSource.addOrUpdateArticleComments(comments.map { articleCommentMapper.map(it) })
    }

    override fun updateArticleComment(comment: ArticleComment, commentsCount: Int) {
        gdDatabaseSource.updateArticleComment(articleCommentMapper.map(comment), commentsCount)
    }

    override fun updateRssSource(checked: Boolean, sourceId: String) {
        gdDatabaseSource.updateRssSource(checked, sourceId)
    }
}
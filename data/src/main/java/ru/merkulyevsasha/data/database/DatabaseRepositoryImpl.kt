package ru.merkulyevsasha.data.database

import io.reactivex.Single
import ru.merkulyevsasha.core.models.Article
import ru.merkulyevsasha.core.models.ArticleComment
import ru.merkulyevsasha.core.models.RssSource
import ru.merkulyevsasha.core.preferences.KeyValueStorage
import ru.merkulyevsasha.core.repositories.DatabaseRepository
import ru.merkulyevsasha.data.database.mappers.ArticleCommentEntityMapper
import ru.merkulyevsasha.data.database.mappers.ArticleCommentMapper
import ru.merkulyevsasha.data.database.mappers.ArticleEntityMapper
import ru.merkulyevsasha.data.database.mappers.ArticleMapper
import ru.merkulyevsasha.data.database.mappers.RssSourceEntityMapper
import ru.merkulyevsasha.data.database.mappers.RssSourceMapper
import java.util.*

class DatabaseRepositoryImpl(
    private val newsDatabaseSource: NewsDatabaseSource,
    keyValueStorage: KeyValueStorage,
    baseUrl: String
) : DatabaseRepository {

    private val articleEntityMapper = ArticleEntityMapper()
    private val articleMapper = ArticleMapper()
    private val articleCommentEntityMapper by lazy { ArticleCommentEntityMapper("bearer " + keyValueStorage.getAccessToken(), baseUrl) }
    private val articleCommentMapper = ArticleCommentMapper()
    private val rssSourceMapper = RssSourceMapper()
    private val rssSourceEntityMapper = RssSourceEntityMapper()

    override fun getArticles(): Single<List<Article>> {
        return newsDatabaseSource.getArticles()
            .flattenAsFlowable { it }
            .map { articleEntityMapper.map(it) }
            .toList()
    }

    override fun searchArticles(searchText: String, byUserActivities: Boolean): Single<List<Article>> {
        return Single.fromCallable { byUserActivities }
            .flatMap { ua ->
                if (ua) newsDatabaseSource.searchUserActivitiesArticles("%${searchText.toLowerCase()}%")
                else newsDatabaseSource.searchArticles("%${searchText.toLowerCase()}%")
            }
            .flattenAsFlowable { it }
            .map { articleEntityMapper.map(it) }
            .toList()
    }

    override fun getArticle(articleId: Int): Single<Article> {
        return newsDatabaseSource.getArticle(articleId)
            .map { articleEntityMapper.map(it) }
    }

    override fun removeOldNotUserActivityArticles(cleanDate: Date) {
        newsDatabaseSource.removeOldNotUserActivityArticles(cleanDate)
    }

    override fun removeOldUserActivityArticles(cleanDate: Date) {
        newsDatabaseSource.removeOldUserActivityArticles(cleanDate)
    }

    override fun getUserActivityArticles(): Single<List<Article>> {
        return newsDatabaseSource.getUserActivityArticles()
            .flattenAsFlowable { it }
            .map { articleEntityMapper.map(it) }
            .toList()
    }

    override fun getArticleComments(articleId: Int): Single<List<ArticleComment>> {
        return newsDatabaseSource.getArticleComments(articleId)
            .flattenAsFlowable { it }
            .map { articleCommentEntityMapper.map(it) }
            .toList()
    }

    override fun saveRssSources(sources: List<RssSource>) {
        newsDatabaseSource.saveRssSources(sources.map { rssSourceMapper.map(it) })
    }

    override fun deleteRssSources() {
        newsDatabaseSource.deleteRssSources()
    }

    override fun getRssSources(): List<RssSource> {
        return newsDatabaseSource.getRssSources()
            .map { rssSourceEntityMapper.map(it) }
    }

    override fun addOrUpdateArticles(articles: List<Article>) {
        newsDatabaseSource.addOrUpdateArticles(articles.map { articleMapper.map(it) })
    }

    override fun updateArticle(article: Article) {
        newsDatabaseSource.updateArticle(articleMapper.map(article))
    }

    override fun addOrUpdateArticleComments(comments: List<ArticleComment>) {
        newsDatabaseSource.addOrUpdateArticleComments(comments.map { articleCommentMapper.map(it) })
    }

    override fun updateArticleComment(comment: ArticleComment, commentsCount: Int) {
        newsDatabaseSource.updateArticleComment(articleCommentMapper.map(comment), commentsCount)
    }

}
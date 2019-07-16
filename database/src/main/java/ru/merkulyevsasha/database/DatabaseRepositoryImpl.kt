package ru.merkulyevsasha.database

import android.content.Context
import androidx.room.Room
import io.reactivex.Single
import ru.merkulyevsasha.core.models.Article
import ru.merkulyevsasha.core.models.ArticleComment
import ru.merkulyevsasha.core.models.RssSource
import ru.merkulyevsasha.core.preferences.KeyValueStorage
import ru.merkulyevsasha.core.repositories.DatabaseRepository
import ru.merkulyevsasha.database.data.Database
import ru.merkulyevsasha.database.mappers.ArticleCommentEntityMapper
import ru.merkulyevsasha.database.mappers.ArticleCommentMapper
import ru.merkulyevsasha.database.mappers.ArticleEntityMapper
import ru.merkulyevsasha.database.mappers.ArticleMapper
import ru.merkulyevsasha.database.mappers.RssSourceEntityMapper
import ru.merkulyevsasha.database.mappers.RssSourceMapper
import java.util.*

class DatabaseRepositoryImpl(context: Context, keyValueStorage: KeyValueStorage) : DatabaseRepository {

    private val articleEntityMapper = ArticleEntityMapper()
    private val articleMapper = ArticleMapper()
    private val articleCommentEntityMapper by lazy { ArticleCommentEntityMapper("bearer " + keyValueStorage.getAccessToken()) }
    private val articleCommentMapper = ArticleCommentMapper()
    private val rssSourceMapper = RssSourceMapper()
    private val rssSourceEntityMapper = RssSourceEntityMapper()

    private val database = Room
        .databaseBuilder(context, Database::class.java, BuildConfig.DB_NAME)
        .fallbackToDestructiveMigration()
        .build()

    override fun getArticles(): Single<List<Article>> {
        return database.articleDao.getArticles()
            .flattenAsFlowable { it }
            .map { articleEntityMapper.map(it) }
            .toList()
    }

    override fun searchArticles(searchText: String, byUserActivities: Boolean): Single<List<Article>> {
        return Single.fromCallable { byUserActivities }
            .flatMap { ua ->
                if (ua) database.articleDao.searchUserActivitiesArticles("%${searchText.toLowerCase()}%")
                else database.articleDao.searchArticles("%${searchText.toLowerCase()}%")
            }
            .flattenAsFlowable { it }
            .map { articleEntityMapper.map(it) }
            .toList()
    }

    override fun getArticle(articleId: Int): Single<Article> {
        return database.articleDao.getArticle(articleId)
            .map { articleEntityMapper.map(it) }
    }

    override fun removeOldNotUserActivityArticles(cleanDate: Date) {
        database.articleDao.removeOldNotUserActivityArticles(cleanDate)
    }

    override fun removeOldUserActivityArticles(cleanDate: Date) {
        database.articleDao.removeOldUserActivityArticles(cleanDate)
    }

    override fun getUserActivityArticles(): Single<List<Article>> {
        return database.articleDao.getUserActivityArticles()
            .flattenAsFlowable { it }
            .map { articleEntityMapper.map(it) }
            .toList()
    }

    override fun getArticleComments(articleId: Int): Single<List<ArticleComment>> {
        return database.articleCommentsDao.getArticleComments(articleId)
            .flattenAsFlowable { it }
            .map { articleCommentEntityMapper.map(it) }
            .toList()
    }

    override fun saveRssSources(sources: List<RssSource>) {
        database.setupDao.saveRssSources(sources.map { rssSourceMapper.map(it) })
    }

    override fun deleteRssSources() {
        database.setupDao.deleteRssSources()
    }

    override fun getRssSources(): List<RssSource> {
        return database.setupDao.getRssSources()
            .map { rssSourceEntityMapper.map(it) }
    }

    override fun addOrUpdateArticles(articles: List<Article>) {
        database.articleDao.insertOrUpdate(articles.map { articleMapper.map(it) })
    }

    override fun updateArticle(article: Article) {
        database.articleDao.update(articleMapper.map(article))
    }

    override fun addOrUpdateArticleComments(comments: List<ArticleComment>) {
        database.articleCommentsDao.insertOrUpdate(comments.map { articleCommentMapper.map(it) })
    }

    override fun updateArticleComment(comment: ArticleComment, commentsCount: Int) {
        database.articleCommentsDao.updateArticleComment(articleCommentMapper.map(comment), commentsCount)
    }

}
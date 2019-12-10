package ru.merkulyevsasha.gddata.database

import io.reactivex.Single
import ru.merkulyevsasha.core.models.Article
import ru.merkulyevsasha.database.entities.AktCommentEntity
import ru.merkulyevsasha.database.entities.AktEntity
import ru.merkulyevsasha.database.entities.ArticleCommentEntity
import ru.merkulyevsasha.database.entities.ArticleEntity
import ru.merkulyevsasha.database.entities.RssSourceEntity
import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gddatabase.data.GdRoomDatabase
import java.util.*

class GdDatabaseSourceImpl(private val database: GdRoomDatabase) : GdDatabaseSource {

    override fun getAkts(): Single<List<Akt>> {
        return database.aktDao.getAkts()
    }

    override fun searchAkts(searchText: String): Single<List<Akt>> {
        return database.aktDao.searchAkts(searchText)
    }

    override fun getAktComments(articleId: Int): Single<List<AktCommentEntity>> {
        return database.aktCommentsDao.getAktComments(articleId)
    }

    override fun addOrUpdateAkts(articles: List<AktEntity>) {
        database.aktDao.insertOrUpdate(articles)
    }

    override fun updateAkt(article: AktEntity) {
        database.aktDao.update(article)
    }

    override fun addOrUpdateAktComments(comments: List<AktCommentEntity>) {
        database.aktCommentsDao.insertOrUpdate(comments)
    }

    override fun updateAktComment(comment: AktCommentEntity, commentsCount: Int) {
        database.aktCommentsDao.update(comment)
    }

    override fun getAkt(articleId: Int): Single<Akt> {
        return database.aktDao.getAkt(articleId)
    }

    override fun getArticles(): Single<List<Article>> {
        return database.articleDao.getArticles()
    }

    override fun searchArticles(searchText: String): Single<List<Article>> {
        return database.articleDao.searchArticles(searchText)
    }

    override fun getArticle(articleId: Int): Single<Article> {
        return database.articleDao.getArticle(articleId)
    }

    override fun getUserActivityArticles(): Single<List<Article>> {
        return database.articleDao.getUserActivityArticles()
    }

    override fun searchUserActivitiesArticles(searchText: String): Single<List<Article>> {
        return database.articleDao.searchUserActivitiesArticles(searchText)
    }

    override fun getSourceArticles(sourceName: String): Single<List<Article>> {
        return database.articleDao.getSourceArticles(sourceName)
    }

    override fun searchSourceArticles(sourceName: String, searchText: String): Single<List<Article>> {
        return database.articleDao.searchSourceArticles(sourceName, searchText)
    }

    override fun getArticleComments(articleId: Int): Single<List<ArticleCommentEntity>> {
        return database.articleCommentsDao.getArticleComments(articleId)
    }

    override fun removeOldNotUserActivityArticles(cleanDate: Date) {
        database.articleDao.removeOldNotUserActivityArticles(cleanDate)
    }

    override fun removeOldUserActivityArticles(cleanDate: Date) {
        database.articleDao.removeOldUserActivityArticles(cleanDate)
    }

    override fun deleteRssSources() {
        database.setupDao.deleteRssSources()
    }

    override fun saveRssSources(sources: List<RssSourceEntity>) {
        database.setupDao.saveRssSources(sources)
    }

    override fun getRssSources(): List<RssSourceEntity> {
        return database.setupDao.getRssSources()
    }

    override fun addOrUpdateArticles(articles: List<ArticleEntity>) {
        database.articleDao.insertOrUpdate(articles)
    }

    override fun updateArticle(article: ArticleEntity) {
        database.articleDao.update(article)
    }

    override fun addOrUpdateArticleComments(comments: List<ArticleCommentEntity>) {
        database.articleCommentsDao.insertOrUpdate(comments)
    }

    override fun updateArticleComment(comment: ArticleCommentEntity, commentsCount: Int) {
        database.articleCommentsDao.updateArticleComment(comment, commentsCount)
    }

    override fun updateRssSource(checked: Boolean, sourceId: String) {
        database.setupDao.updateRssSource(checked, sourceId)
    }

}
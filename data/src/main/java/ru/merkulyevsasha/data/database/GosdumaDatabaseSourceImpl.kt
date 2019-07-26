package ru.merkulyevsasha.data.database

import io.reactivex.Single
import ru.merkulyevsasha.database.data.GosdumaRoomDatabase
import ru.merkulyevsasha.database.entities.AktCommentEntity
import ru.merkulyevsasha.database.entities.AktEntity
import ru.merkulyevsasha.database.entities.ArticleCommentEntity
import ru.merkulyevsasha.database.entities.ArticleEntity
import ru.merkulyevsasha.database.entities.RssSourceEntity
import java.util.*

class GosdumaDatabaseSourceImpl(private val database: GosdumaRoomDatabase) : GosdumaDatabaseSource {

    override fun getArticles(): Single<List<ArticleEntity>> {
        return database.articleDao.getArticles()
    }

    override fun getArticle(articleId: Int): Single<ArticleEntity> {
        return database.articleDao.getArticle(articleId)
    }

    override fun searchUserActivitiesArticles(searchText: String): Single<List<ArticleEntity>> {
        return database.articleDao.searchUserActivitiesArticles(searchText)
    }

    override fun searchArticles(searchText: String): Single<List<ArticleEntity>> {
        return database.articleDao.searchArticles(searchText)
    }

    override fun removeOldNotUserActivityArticles(cleanDate: Date) {
        database.articleDao.removeOldNotUserActivityArticles(cleanDate)
    }

    override fun removeOldUserActivityArticles(cleanDate: Date) {
        database.articleDao.removeOldUserActivityArticles(cleanDate)
    }

    override fun getUserActivityArticles(): Single<List<ArticleEntity>> {
        return database.articleDao.getUserActivityArticles()
    }

    override fun getArticleComments(articleId: Int): Single<List<ArticleCommentEntity>> {
        return database.articleCommentsDao.getArticleComments(articleId)
    }

    override fun saveRssSources(sources: List<RssSourceEntity>) {
        database.setupDao.saveRssSources(sources)
    }

    override fun deleteRssSources() {
        database.setupDao.deleteRssSources()
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

    override fun getAkts(): Single<List<AktEntity>> {
        return database.aktDao.getAkts()
    }

    override fun searchAkts(searchText: String): Single<List<AktEntity>> {
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

    override fun getAkt(articleId: Int): Single<AktEntity> {
        return database.aktDao.getAkt(articleId)
    }

}
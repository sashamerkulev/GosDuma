package ru.merkulyevsasha.data.database

import io.reactivex.Single
import ru.merkulyevsasha.database.data.NewsRoomDatabase
import ru.merkulyevsasha.database.entities.ArticleCommentEntity
import ru.merkulyevsasha.database.entities.ArticleEntity
import ru.merkulyevsasha.database.entities.RssSourceEntity
import java.util.*

class NewsDatabaseSourceImpl(private val database: NewsRoomDatabase) : NewsDatabaseSource {

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

}
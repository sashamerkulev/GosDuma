package ru.merkulyevsasha.database

import io.reactivex.Single
import ru.merkulyevsasha.database.entities.ArticleCommentEntity
import ru.merkulyevsasha.database.entities.ArticleEntity
import ru.merkulyevsasha.database.entities.RssSourceEntity
import java.util.*

interface NewsDatabaseSource {
    fun getArticles(): Single<List<ArticleEntity>>
    fun searchUserActivitiesArticles(searchText: String): Single<List<ArticleEntity>>
    fun searchArticles(searchText: String): Single<List<ArticleEntity>>
    fun removeOldNotUserActivityArticles(cleanDate: Date)
    fun removeOldUserActivityArticles(cleanDate: Date)
    fun getUserActivityArticles(): Single<List<ArticleEntity>>
    fun getArticleComments(articleId: Int): Single<List<ArticleCommentEntity>>
    fun saveRssSources(sources: List<RssSourceEntity>)
    fun deleteRssSources()
    fun getRssSources(): List<RssSourceEntity>
    fun addOrUpdateArticles(articles: List<ArticleEntity>)
    fun updateArticle(article: ArticleEntity)
    fun addOrUpdateArticleComments(comments: List<ArticleCommentEntity>)
    fun updateArticleComment(comment: ArticleCommentEntity, commentsCount: Int)
    fun getArticle(articleId: Int): Single<ArticleEntity>
}
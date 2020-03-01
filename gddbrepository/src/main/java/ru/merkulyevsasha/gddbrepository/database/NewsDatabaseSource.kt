package ru.merkulyevsasha.gddbrepository.database

import io.reactivex.Single
import ru.merkulyevsasha.core.models.Article
import ru.merkulyevsasha.gddatabase.entities.ArticleCommentEntity
import ru.merkulyevsasha.gddatabase.entities.ArticleEntity
import ru.merkulyevsasha.gddatabase.entities.RssSourceEntity
import java.util.*

interface NewsDatabaseSource {
    fun getArticles(): Single<List<Article>>
    fun searchArticles(searchText: String): Single<List<Article>>

    fun getArticle(articleId: Int): Single<Article>

    fun getUserActivityArticles(): Single<List<Article>>
    fun searchUserActivitiesArticles(searchText: String): Single<List<Article>>

    fun getSourceArticles(sourceName: String): Single<List<Article>>
    fun searchSourceArticles(sourceName: String, searchText: String): Single<List<Article>>

    fun getArticleComments(articleId: Int): Single<List<ArticleCommentEntity>>

    fun removeOldNotUserActivityArticles(cleanDate: Date)
    fun removeOldUserActivityArticles(cleanDate: Date)

    fun deleteRssSources()
    fun saveRssSources(sources: List<RssSourceEntity>)
    fun getRssSources(): List<RssSourceEntity>

    fun addOrUpdateArticles(articles: List<ArticleEntity>)

    fun updateArticle(article: ArticleEntity)
    fun addOrUpdateArticleComments(comments: List<ArticleCommentEntity>)
    fun updateArticleComment(comment: ArticleCommentEntity, commentsCount: Int)
    fun updateRssSource(checked: Boolean, sourceId: String)
}
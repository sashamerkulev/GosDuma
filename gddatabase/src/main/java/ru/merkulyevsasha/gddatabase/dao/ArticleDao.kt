package ru.merkulyevsasha.gddatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Single
import ru.merkulyevsasha.core.models.Article
import ru.merkulyevsasha.gddatabase.entities.ArticleEntity
import java.util.*

@Dao
interface ArticleDao {
    @Query("select a.articleId, a.sourceId, s.sourceName, a.title, a.link, a.description, a.pubDate, a.lastActivityDate, a.category, a.pictureUrl, a.usersLikeCount, a.usersDislikeCount, a.usersCommentCount, a.isUserLiked, a.isUserDisliked, a.isUserCommented from articles a join sources s on s.sourceId = a.sourceId where s.checked order by a.pubDate desc limit 100")
    fun getArticles(): Single<List<Article>>

    @Query("select a.articleId, a.sourceId, s.sourceName, a.title, a.link, a.description, a.pubDate, a.lastActivityDate, a.category, a.pictureUrl, a.usersLikeCount, a.usersDislikeCount, a.usersCommentCount, a.isUserLiked, a.isUserDisliked, a.isUserCommented from articles a join sources s on s.sourceId = a.sourceId where s.checked and  a.search like :searchText order by a.pubDate desc limit 100")
    fun searchArticles(searchText: String): Single<List<Article>>

    @Query("select a.articleId, a.sourceId, s.sourceName, a.title, a.link, a.description, a.pubDate, a.lastActivityDate, a.category, a.pictureUrl, a.usersLikeCount, a.usersDislikeCount, a.usersCommentCount, a.isUserLiked, a.isUserDisliked, a.isUserCommented from articles a join sources s on s.sourceId = a.sourceId where a.articleId = :articleId")
    fun getArticle(articleId: Int): Single<Article>

    @Query("select a.articleId, a.sourceId, s.sourceName, a.title, a.link, a.description, a.pubDate, a.lastActivityDate, a.category, a.pictureUrl, a.usersLikeCount, a.usersDislikeCount, a.usersCommentCount, a.isUserLiked, a.isUserDisliked, a.isUserCommented from articles a join sources s on s.sourceId = a.sourceId where a.isUserLiked or a.isUserCommented or a.isUserDisliked order by a.pubDate desc  limit 100")
    fun getUserActivityArticles(): Single<List<Article>>

    @Query("select a.articleId, a.sourceId, s.sourceName, a.title, a.link, a.description, a.pubDate, a.lastActivityDate, a.category, a.pictureUrl, a.usersLikeCount, a.usersDislikeCount, a.usersCommentCount, a.isUserLiked, a.isUserDisliked, a.isUserCommented from articles a join sources s on s.sourceId = a.sourceId where a.search like :searchText and (a.isUserLiked or a.isUserCommented or a.isUserDisliked) order by a.pubDate desc  limit 100")
    fun searchUserActivitiesArticles(searchText: String): Single<List<Article>>

    @Query("select a.articleId, a.sourceId, s.sourceName, a.title, a.link, a.description, a.pubDate, a.lastActivityDate, a.category, a.pictureUrl, a.usersLikeCount, a.usersDislikeCount, a.usersCommentCount, a.isUserLiked, a.isUserDisliked, a.isUserCommented from articles a join sources s on s.sourceId = a.sourceId where a.sourceId = :sourceId order by a.pubDate desc")
    fun getSourceArticles(sourceId: String): Single<List<Article>>

    @Query("select a.articleId, a.sourceId, s.sourceName, a.title, a.link, a.description, a.pubDate, a.lastActivityDate, a.category, a.pictureUrl, a.usersLikeCount, a.usersDislikeCount, a.usersCommentCount, a.isUserLiked, a.isUserDisliked, a.isUserCommented from articles a join sources s on s.sourceId = a.sourceId where a.search like :searchText and a.sourceId = :sourceId order by a.pubDate desc")
    fun searchSourceArticles(sourceId: String, searchText: String): Single<List<Article>>

    @Query("delete from articles where pubDate < :cleanDate and lastActivityDate < :cleanDate " +
        "and not (isUserLiked or isUserCommented or isUserDisliked)")
    fun removeOldNotUserActivityArticles(cleanDate: Date)

    @Query("delete from articles where pubDate < :cleanDate and lastActivityDate < :cleanDate " +
        "and (isUserLiked or isUserCommented or isUserDisliked)")
    fun removeOldUserActivityArticles(cleanDate: Date)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(items: List<ArticleEntity>)

    @Update
    fun update(item: ArticleEntity)
}

package ru.merkulyevsasha.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import io.reactivex.Single
import ru.merkulyevsasha.database.entities.ArticleCommentEntity

@Dao
interface ArticleCommentsDao {
    @Query("select * from comments where articleId = :articleId order by pubDate desc")
    fun getArticleComments(articleId: Int): Single<List<ArticleCommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(items: List<ArticleCommentEntity>)

    @Update
    fun update(item: ArticleCommentEntity)

    @Query("update articles set usersCommentCount = usersCommentCount + :commentsCount, isUserCommented = 1 where articleId = :articleId")
    fun updateArticle(articleId: Int, commentsCount: Int)

    @Transaction
    fun updateArticleComment(comment: ArticleCommentEntity, commentsCount: Int) {
        update(comment)
        updateArticle(comment.articleId, commentsCount)
    }

}

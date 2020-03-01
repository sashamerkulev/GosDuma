package ru.merkulyevsasha.gddatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import io.reactivex.Single
import ru.merkulyevsasha.gddatabase.entities.AktCommentEntity

@Dao
interface AktCommentsDao {
    @Query("select * from aktComments where aktId = :aktId order by pubDate desc")
    fun getAktComments(aktId: Int): Single<List<AktCommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(items: List<AktCommentEntity>)

    @Update
    fun update(item: AktCommentEntity)

    @Query("update akts set usersCommentCount = usersCommentCount + :commentsCount, isUserCommented = 1 where aktId = :aktId")
    fun updateArticle(aktId: Int, commentsCount: Int)

    @Transaction
    fun updateArticleComment(comment: AktCommentEntity, commentsCount: Int) {
        update(comment)
        updateArticle(comment.aktId, commentsCount)
    }

}

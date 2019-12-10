package ru.merkulyevsasha.gddatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Single
import ru.merkulyevsasha.database.entities.AktEntity
import ru.merkulyevsasha.gdcore.models.Akt

@Dao
interface AktDao {
    @Query("select a.aktId, a.sourceId, s.sourceName, a.title, a.link, a.description, a.pubDate, a.lastActivityDate, a.category, a.pictureUrl, a.usersLikeCount, a.usersDislikeCount, a.usersCommentCount, a.isUserLiked, a.isUserDisliked, a.isUserCommented from akts a join sources s on s.sourceId = a.sourceId where s.checked order by a.pubDate desc ")
    fun getAkts(): Single<List<Akt>>

    @Query("select a.aktId, a.sourceId, s.sourceName, a.title, a.link, a.description, a.pubDate, a.lastActivityDate, a.category, a.pictureUrl, a.usersLikeCount, a.usersDislikeCount, a.usersCommentCount, a.isUserLiked, a.isUserDisliked, a.isUserCommented from akts a join sources s on s.sourceId = a.sourceId where s.checked and  a.search like :searchText order by a.pubDate desc")
    fun searchAkts(searchText: String): Single<List<Akt>>

    @Query("select a.aktId, a.sourceId, s.sourceName, a.title, a.link, a.description, a.pubDate, a.lastActivityDate, a.category, a.pictureUrl, a.usersLikeCount, a.usersDislikeCount, a.usersCommentCount, a.isUserLiked, a.isUserDisliked, a.isUserCommented from akts a join sources s on s.sourceId = a.sourceId where a.aktId = :aktId order by a.pubDate desc")
    fun getAkt(aktId: Int): Single<Akt>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(items: List<AktEntity>)

    @Update
    fun update(item: AktEntity)
}

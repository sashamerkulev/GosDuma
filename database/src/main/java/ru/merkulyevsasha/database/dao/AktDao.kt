package ru.merkulyevsasha.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Single
import ru.merkulyevsasha.database.entities.AktEntity

@Dao
interface AktDao {
    @Query("select * from akts order by pubDate desc")
    fun getAkts(): Single<List<AktEntity>>

    @Query("select * from akts where search like :searchText order by pubDate desc")
    fun searchAkts(searchText: String): Single<List<AktEntity>>

    @Query("select * from akts where articleId = :articleId order by pubDate desc")
    fun getAkt(articleId: Int): Single<AktEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(items: List<AktEntity>)

    @Update
    fun update(item: AktEntity)
}

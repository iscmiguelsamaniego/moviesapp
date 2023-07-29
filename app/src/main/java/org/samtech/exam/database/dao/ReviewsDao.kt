package org.samtech.exam.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.samtech.exam.database.entities.Reviews

@Dao
interface ReviewsDao {
    @Query("SELECT COUNT(*) FROM reviews")
    fun getReviewsCount(): Int

    @Query("SELECT * FROM reviews ORDER BY id DESC")
    fun getReviews(): Flow<List<Reviews>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(reviews: Reviews)

}
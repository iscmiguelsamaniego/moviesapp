package org.samtech.exam.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.samtech.exam.database.entities.Results

@Dao
interface ResultsDao {
    @Query("SELECT COUNT(*) FROM results")
    fun getResultsCount(): Int

    @Query("SELECT * FROM results ORDER BY id DESC")
    fun getResults(): Flow<List<Results>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(results: Results)

    @Delete
    suspend fun deletResults(results: Results)

    @Query("DELETE FROM results")
    suspend fun deleteAll()
}
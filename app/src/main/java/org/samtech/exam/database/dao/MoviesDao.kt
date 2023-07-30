package org.samtech.exam.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.samtech.exam.database.entities.Movies


@Dao
interface MoviesDao {
    @Query("SELECT COUNT(*) FROM movies")
    fun getMoviesCount(): Int

    @Query("SELECT * FROM movies ORDER BY id DESC")
    fun getMovies(): Flow<List<Movies>>

    @Query("SELECT * FROM movies where type = :type")
    fun getByType(type: String?):Flow<List<Movies>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movies: Movies)

}
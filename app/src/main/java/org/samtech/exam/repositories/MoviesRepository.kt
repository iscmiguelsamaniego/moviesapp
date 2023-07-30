package org.samtech.exam.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import org.samtech.exam.database.dao.MoviesDao
import org.samtech.exam.database.entities.Movies

class MoviesRepository(private val moviesDao: MoviesDao) {

    fun getAllMovies(): Flow<List<Movies>> {
        return moviesDao.getMovies()
    }

    fun getMovieByType(paramType: String): Flow<List<Movies>> {
        return moviesDao.getByType(paramType)
    }


    @WorkerThread
    suspend fun insert(movies: Movies) {
        moviesDao.insert(movies)
    }

    suspend fun getCount(): Int {
        return moviesDao.getMoviesCount()
    }
}
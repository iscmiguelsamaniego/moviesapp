package org.samtech.exam.repositories

import androidx.annotation.WorkerThread
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.samtech.exam.database.dao.ReviewsDao
import org.samtech.exam.database.entities.Reviews

class ReviewsRepositor(private val reviewsDao: ReviewsDao) {

    fun getReviews(): Flow<List<Reviews>> {
        return reviewsDao.getReviews()
    }

    @WorkerThread
    suspend fun insert(reviews : Reviews){
        reviewsDao.insert(reviews)
    }

    @WorkerThread
    suspend fun deleteAll(){
        reviewsDao.deleteAll()
    }

    fun getCount() : Flow<List<Int>> {
        return reviewsDao.getReviewsCount()
    }
}
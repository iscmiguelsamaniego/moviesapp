package org.samtech.exam.repositories

import androidx.annotation.WorkerThread
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

    suspend fun getCount() : Int{
        return reviewsDao.getReviewsCount()
    }
}
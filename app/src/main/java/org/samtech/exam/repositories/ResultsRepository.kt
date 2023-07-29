package org.samtech.exam.repositories

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import org.samtech.exam.database.dao.ResultsDao
import org.samtech.exam.database.entities.Results

class ResultsRepository(private val resultsDao : ResultsDao) {

    fun getResults(): Flow<List<Results>>{
        return resultsDao.getResults()
    }

    @WorkerThread
    suspend fun insert(results : Results){
        resultsDao.insert(results)
    }

    suspend fun getCount() : Int{
        return resultsDao.getResultsCount()
    }
}
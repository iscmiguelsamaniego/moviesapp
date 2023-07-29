package org.samtech.exam.repositories

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import org.samtech.exam.database.dao.UsersDao
import org.samtech.exam.database.entities.Users


class UsersRepository(private val usersDao : UsersDao) {

    fun getUsers(): Flow<List<Users>>{
        return usersDao.getUsers()
    }

    @WorkerThread
    suspend fun insert(users : Users){
        usersDao.insert(users)
    }

    suspend fun getCount() : Int{
        return usersDao.getUsersCount()
    }
}
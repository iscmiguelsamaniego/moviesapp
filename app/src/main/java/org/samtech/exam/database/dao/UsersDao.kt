package org.samtech.exam.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.samtech.exam.database.entities.Users

@Dao
interface UsersDao {
    @Query("SELECT COUNT(*) FROM users")
    fun getUsersCount(): Int

    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getUsers(): Flow<List<Users>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(users: Users)

}
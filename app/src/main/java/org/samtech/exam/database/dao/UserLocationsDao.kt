package org.samtech.exam.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.samtech.exam.database.entities.UserLocation
import java.util.UUID

@Dao
interface UserLocationsDao {

    @Query("SELECT * FROM locations ORDER BY date DESC")
    fun getLocations(): LiveData<List<UserLocation>>

    @Query("SELECT * FROM locations WHERE id=(:id)")
    fun getLocation(id: UUID): LiveData<UserLocation>

    @Update
    fun updateLocation(userLocation: UserLocation)

    @Insert
    fun addLocation(userLocation: UserLocation)

    @Insert
    fun addLocations(locations: List<UserLocation>)
}
package org.samtech.exam.repositories

import android.content.Context
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.samtech.exam.database.MoviesRoomDatabase
import org.samtech.exam.database.entities.UserLocation
import org.samtech.exam.utils.MyLocationManager
import java.util.UUID
import java.util.concurrent.ExecutorService

class LocationsRepository(
    private val moviesRoomDB: MoviesRoomDatabase,
    private val myLocationManager: MyLocationManager,
    private val executor: ExecutorService
) {

    private val userLocationsDao = moviesRoomDB.locationsDao()

    fun getLocations(): LiveData<List<UserLocation>> = userLocationsDao.getLocations()

    fun getLocation(id: UUID): LiveData<UserLocation> = userLocationsDao.getLocation(id)

    fun updateLocation(myLocationEntity: UserLocation) {
        executor.execute {
            userLocationsDao.updateLocation(myLocationEntity)
        }
    }

    fun addLocation(myLocationEntity: UserLocation) {
        executor.execute {
            userLocationsDao.addLocation(myLocationEntity)
        }
    }

    fun addLocations(myLocationEntities: List<UserLocation>) {
        executor.execute {
            userLocationsDao.addLocations(myLocationEntities)
        }
    }

    val receivingLocationUpdates: LiveData<Boolean> = myLocationManager.receivingLocationUpdates

    @MainThread
    fun startLocationUpdates() = myLocationManager.startLocationUpdates()

    @MainThread
    fun stopLocationUpdates() = myLocationManager.stopLocationUpdates()

    companion object {
        @Volatile
        private var INSTANCE: LocationsRepository? = null

        fun getInstance(
            context: Context,
            executor: ExecutorService
        ): LocationsRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LocationsRepository(
                    MoviesRoomDatabase.getDataBase(context, CoroutineScope(SupervisorJob())),
                    MyLocationManager.getInstance(context),
                    executor
                )
                    .also { INSTANCE = it }
            }
        }
    }
}
package org.samtech.exam.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.samtech.exam.database.converters.LocationTypeConverters
import org.samtech.exam.database.dao.UserLocationsDao
import org.samtech.exam.database.dao.MoviesDao
import org.samtech.exam.database.dao.ReviewsDao
import org.samtech.exam.database.dao.UsersDao
import org.samtech.exam.database.entities.UserLocation
import org.samtech.exam.database.entities.Movies
import org.samtech.exam.database.entities.Reviews
import org.samtech.exam.database.entities.Users

@Database(
    entities = [Users::class, Movies::class, Reviews::class, UserLocation::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocationTypeConverters::class)
abstract class MoviesRoomDatabase : RoomDatabase() {

    abstract fun resultsDao(): MoviesDao
    abstract fun usersDao(): UsersDao
    abstract fun reviewsDao(): ReviewsDao
    abstract fun locationsDao(): UserLocationsDao

    companion object {
        @Volatile
        private var INSTANCE: MoviesRoomDatabase? = null

        fun getDataBase(
            context: Context, scope: CoroutineScope
        ): MoviesRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoviesRoomDatabase::class.java,
                    "movies_database"
                )
                    .addCallback(MoviesDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class MoviesDatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                INSTANCE?.let { database ->

                    scope.launch {
                        database.clearAllTables()
                    }
                }
            }
        }
    }
}
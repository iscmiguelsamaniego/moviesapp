package org.samtech.exam.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat
import java.util.Date
import java.util.UUID

@Entity(tableName = "locations")
data class UserLocation(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val foreground: Boolean = true,
    val date: Date = Date()
) {
    override fun toString(): String {
        val appState = if (foreground) {
            "en app"
        } else {
            "en Segundo Plano"
        }

        return "$latitude, $longitude $appState " + "${
            DateFormat.getDateTimeInstance().format(date)
        }.\n"
    }
}
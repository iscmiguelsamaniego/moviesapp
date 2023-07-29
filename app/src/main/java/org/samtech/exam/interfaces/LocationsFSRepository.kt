package org.samtech.exam.interfaces

import org.samtech.exam.firebase.models.FSLocations
import org.samtech.exam.network.pokos.UserPoko

interface LocationsFSRepository {

    fun getLocationsValues(listener: LocationsListener) : String
    fun updateLocationsValues(userId : String, user: UserPoko) : String
    fun storeLocationsValues(user: UserPoko): String

    interface LocationsListener {
        fun onLocationsResult(locations: FSLocations)
    }
}
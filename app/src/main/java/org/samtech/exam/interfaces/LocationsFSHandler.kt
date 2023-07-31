package org.samtech.exam.interfaces

import org.samtech.exam.firebase.models.FSLocation

interface LocationsFSHandler {
    fun getLocation(listener: LocationsListener): String
    fun updateLocation(locationId: String, location: FSLocation): String
    fun storeLocation(location: FSLocation): String

    interface LocationsListener {
        fun onLocationsResult(locations: ArrayList<FSLocation>)
    }
}
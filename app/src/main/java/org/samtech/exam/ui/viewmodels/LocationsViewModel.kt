package org.samtech.exam.ui.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.samtech.exam.firebase.models.FSLocation
import org.samtech.exam.firebase.repositories.LocationsFSRepository
import org.samtech.exam.interfaces.LocationsFSHandler
import org.samtech.exam.repositories.LocationsRepository
import java.util.concurrent.Executors

class LocationsViewModel(
    application: Application,
    locationsFSRepo: LocationsFSRepository
) : ViewModel() {

    private val locationsFSHandler: LocationsFSHandler = locationsFSRepo

    private val locationRepository = LocationsRepository.getInstance(
        application.applicationContext,
        Executors.newSingleThreadExecutor()
    )
    val receivingLocationUpdates: LiveData<Boolean> = locationRepository.receivingLocationUpdates
    val locationListLiveData = locationRepository.getLocations()

    fun startLocationUpdates() = locationRepository.startLocationUpdates()

    fun stopLocationUpdates() = locationRepository.stopLocationUpdates()


    fun getLocationsFSValues(): MutableLiveData<ArrayList<FSLocation>> {
        val locationsMLD = MutableLiveData<ArrayList<FSLocation>>()

        locationsFSHandler.getLocation(object : LocationsFSHandler.LocationsListener {
            override fun onLocationsResult(locations: ArrayList<FSLocation>) {
                locationsMLD.postValue(locations)
            }
        })

        return locationsMLD
    }

    fun storeLocations(paramLocation: FSLocation): String {
        return locationsFSHandler.storeLocation(paramLocation)
    }

    @Suppress("UNCHECKED_CAST")
    class LocationsViewModelFactory(
        private val application: Application,
        private val locationsFSRepo: LocationsFSRepository
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LocationsViewModel::class.java)) {
                return LocationsViewModel(application, locationsFSRepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}



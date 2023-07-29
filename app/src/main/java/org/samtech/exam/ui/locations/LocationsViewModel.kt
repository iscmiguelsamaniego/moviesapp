package org.samtech.exam.ui.locations

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.samtech.exam.Singleton
import org.samtech.exam.firebase.models.FSLocations
import org.samtech.exam.firebase.repositories.FireStoreLocationsFSRepository
import org.samtech.exam.interfaces.LocationsFSRepository
import org.samtech.exam.network.volley.APIController
import org.samtech.exam.network.volley.ServiceListenerVolley
import org.samtech.exam.repositories.LocationsRepository
import org.samtech.exam.utils.Constants
import org.samtech.exam.utils.NetworkUtils
import java.util.concurrent.Executors

class LocationsViewModel(
    application: Application,
    locationsFSRepo: FireStoreLocationsFSRepository
) : ViewModel() {

    private val locationsFSRepository: LocationsFSRepository = locationsFSRepo

    private val locationRepository = LocationsRepository.getInstance(
        application.applicationContext,
        Executors.newSingleThreadExecutor()
    )
    val receivingLocationUpdates: LiveData<Boolean> = locationRepository.receivingLocationUpdates
    val locationListLiveData = locationRepository.getLocations()

    fun startLocationUpdates() = locationRepository.startLocationUpdates()

    fun stopLocationUpdates() = locationRepository.stopLocationUpdates()


    fun getLocationsFSValues(): MutableLiveData<FSLocations> {
        val locationsMLD = MutableLiveData<FSLocations>()

        locationsFSRepository.getLocationsValues(object : LocationsFSRepository.LocationsListener {
            override fun onLocationsResult(locations: FSLocations) {
                locationsMLD.postValue(locations)
            }
        })

        return locationsMLD
    }

    //this is agood sample
    fun downloadAndStoreOrUpdateLocation(documentId: String) {
        val ctx = Singleton.instance?.applicationContext
        if (ctx != null) {
            if (NetworkUtils.isOnline(ctx)) {
                val service = ServiceListenerVolley()
                val apiController = APIController(service)

                apiController.getString(Constants.PROFILE_PATH, Constants.TOKEN) { response ->
                    if (!response.isNullOrBlank()) {
                        //TODO Store locations with model

                        /*val userPokoResponse = Gson().fromJson(response, UserPoko::class.java)
                        if (documentId.isBlank()) {
                            locationsRepository.storeLocationsValues(userPokoResponse)
                        } else {
                            locationsRepository.updateLocationsValues(documentId, userPokoResponse)
                        }*/
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class LocationsViewModelFactory(
        private val application: Application,
        private val locationsFSRepo: FireStoreLocationsFSRepository
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
package org.samtech.exam.ui.locations

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.samtech.exam.Singleton
import org.samtech.exam.firebase.models.FSLocations
import org.samtech.exam.firebase.repositories.FireStoreLocationsRepository
import org.samtech.exam.interfaces.LocationsRepository
import org.samtech.exam.network.volley.APIController
import org.samtech.exam.network.volley.ServiceListenerVolley
import org.samtech.exam.utils.Constants
import org.samtech.exam.utils.NetworkUtils

class NotificationsViewModel (private val locationsRepo: FireStoreLocationsRepository) : ViewModel(){

    private val locationsRepository: LocationsRepository = locationsRepo

    fun getLocationsFSValues(): MutableLiveData<FSLocations> {
        val locationsMLD = MutableLiveData<FSLocations>()

        locationsRepository.getLocationsValues(object : LocationsRepository.LocationsListener {
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
        private val locationRepo: FireStoreLocationsRepository
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
                return NotificationsViewModel(locationRepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
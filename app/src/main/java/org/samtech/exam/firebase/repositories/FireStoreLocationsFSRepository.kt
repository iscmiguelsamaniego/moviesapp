package org.samtech.exam.firebase.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import org.samtech.exam.firebase.models.FSLocation
import org.samtech.exam.interfaces.LocationsFSHandler
import org.samtech.exam.utils.Constants.COLLECTION_LOCATIONS
import org.samtech.exam.utils.Constants.STORED_MSG
import org.samtech.exam.utils.Constants.UPDATED_MSG

class FireStoreLocationsFSRepository : LocationsFSHandler {

    var db = FirebaseFirestore.getInstance()
    override fun getLocation(listener: LocationsFSHandler.LocationsListener): String {
        var getResult = ""
        var locations  = arrayListOf<FSLocation>()
        db.collection(COLLECTION_LOCATIONS)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    for (document in result) {
                        val gson = Gson()
                        val jsonLocation =
                            gson.toJson(document.data).toString()
                        val location =
                            gson.fromJson(jsonLocation, FSLocation::class.java)

                        locations.add(
                            FSLocation(
                                locationId = document.id,
                                latitude = location.latitude,
                                longitude = location.longitude,
                                date = location.date
                            )
                        )
                    }
                    listener.onLocationsResult(locations)
                }
            }
            .addOnFailureListener { e ->
                getResult = "Error : $e.cause"
            }

        return getResult
    }

    override fun updateLocation(locationId: String, location: FSLocation): String {
        var updateResult = ""

        db.collection(COLLECTION_LOCATIONS).document(locationId).set(location)
            .addOnSuccessListener {
                updateResult = UPDATED_MSG
            }
            .addOnFailureListener { e ->
                updateResult = "Error : $e.cause"
            }

        return updateResult
    }

    override fun storeLocation(location: FSLocation): String {
        var storeResult = ""

        db.collection(COLLECTION_LOCATIONS)
            .add(location)
            .addOnSuccessListener {
                storeResult = STORED_MSG
            }
            .addOnFailureListener { e ->
                storeResult = "Error : $e.cause"
            }
        return storeResult
    }
}
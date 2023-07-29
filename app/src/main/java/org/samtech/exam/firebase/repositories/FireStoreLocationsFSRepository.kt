package org.samtech.exam.firebase.repositories

import com.google.firebase.firestore.FirebaseFirestore
import org.samtech.exam.firebase.models.FSLocations
import org.samtech.exam.interfaces.LocationsFSRepository
import org.samtech.exam.network.pokos.UserPoko
import org.samtech.exam.utils.Constants.COLLECTION_USER

class FireStoreLocationsFSRepository : LocationsFSRepository {

    var db = FirebaseFirestore.getInstance()

    /*
       /*if (it.fsDocumentId == null) {
       //TODO IMPLEMENT THIS FOR UPDATE ON FIRESTORE

                    opUserViewModel.downloadAndStoreOrUpdateUser("")
                } else {
                    opUserViewModel.downloadAndStoreOrUpdateUser(it.fsDocumentId!!)
                }*/

                val urlImage = BASE_IMAGE_PATH + user.avatar!!.tmdb.avatarPath!!
                setGlideImage(ctx, urlImage, userIView)

                val adultResponse =
                    if (it.includeAdult == true)
                        getString(R.string.yes) else getString(R.string.no)

                val nameRespose =
                    if(it.name.isNullOrBlank())
                        getString(R.string.no_registered) else it.name

                val x = getSpannedText(getString(R.string.user_values,
                    it.username,
                    it.id.toString(),
                    it.iso31661,
                    it.iso6391,
                    nameRespose,
                    adultResponse))
                userInfoTView.text = x
    * */
    override fun getLocationsValues(listener: LocationsFSRepository.LocationsListener): String {
        var getResult = ""

        db.collection(COLLECTION_USER)
            .get()
            .addOnSuccessListener { result ->
                if(!result.isEmpty) {
                  /*  val gson = Gson()
                    for (document in result) {
                        val user = gson.toJson(document.data).toString()
                        val userAux = gson.fromJson(user, FSLocations::class.java)
                        val avatarObj = gson.toJson(document.data.getValue(KEY_AVATAR)).toString()
                        val avatar = gson.fromJson(avatarObj, Avatar::class.java)

                        listener.onLocationsResult(
                            FSLocations(
                                fsDocumentId = document.id,
                                avatar = avatar,
                                id = userAux.id,
                                iso6391 = userAux.iso6391,
                                iso31661 = userAux.iso31661,
                                name = userAux.name,
                                includeAdult = userAux.includeAdult,
                                username = userAux.username
                            )
                        )
                    }*/
                }else{
                    listener.onLocationsResult(FSLocations())
                }
            }
            .addOnFailureListener { e ->
                getResult = "Error : $e.cause"
            }

        return getResult
    }

    override fun updateLocationsValues(userId: String, user: UserPoko): String {
        var updateResult = ""

        db.collection(COLLECTION_USER).document(userId).set(user)
            .addOnSuccessListener {
                updateResult = "Valores actualizados correctamente"
            }
            .addOnFailureListener { e ->
                updateResult = "Error : $e.cause"
            }

        return updateResult
    }

    override fun storeLocationsValues(user: UserPoko): String {
        var storeResult = ""

        db.collection(COLLECTION_USER)
            .add(user)
            .addOnSuccessListener { documentReference ->
                storeResult = "Datos almacenados correctamente"
            }
            .addOnFailureListener { e ->
                storeResult = "Error : $e.cause"
            }
        return storeResult
    }
}
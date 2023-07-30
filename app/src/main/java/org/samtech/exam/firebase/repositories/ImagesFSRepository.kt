package org.samtech.exam.firebase.repositories

import com.google.firebase.firestore.FirebaseFirestore
import org.samtech.exam.interfaces.ImagesFSHandler
import org.samtech.exam.network.pokos.UserPoko

class ImagesFSRepository : ImagesFSHandler{

    //TODO PENDING
    override fun getImageValues(listener: ImagesFSHandler.ImageListener): String {
        TODO("Not yet implemented")
    }

    override fun updateImageValues(userId: String, user: UserPoko): String {
        TODO("Not yet implemented")
    }

    override fun storeImageValues(user: UserPoko): String {
        TODO("Not yet implemented")
    }


}
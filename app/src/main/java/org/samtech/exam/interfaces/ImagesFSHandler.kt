package org.samtech.exam.interfaces

import org.samtech.exam.firebase.models.FSLocations
import org.samtech.exam.network.pokos.UserPoko

interface ImagesFSHandler {

    fun getImageValues(listener: ImageListener) : String
    fun updateImageValues(userId : String, user: UserPoko) : String
    fun storeImageValues(user: UserPoko): String

    interface ImageListener {

    }
}
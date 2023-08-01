package org.samtech.exam.interfaces

import android.graphics.Bitmap
import android.net.Uri
import org.samtech.exam.firebase.models.FSLocation
import org.samtech.exam.firebase.models.FSPhotos
import org.samtech.exam.network.pokos.UserPoko

interface PhotosFSHandler {

    fun getImageValues(listener: CustomPhotosListener) : String
    fun updateImageValues(userId : String, user: UserPoko) : String
    fun storeImageValues(path: String, uri: Uri?): String

    fun storeImageFromByte(paramBitMap: Bitmap): String

    interface CustomPhotosListener {
        fun onCustomPhotosResult(photos: ArrayList<FSPhotos>)
    }
}
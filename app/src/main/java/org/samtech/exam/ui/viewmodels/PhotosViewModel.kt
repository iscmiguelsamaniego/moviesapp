package org.samtech.exam.ui.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.samtech.exam.firebase.repositories.PhotosFSRepository
import org.samtech.exam.interfaces.PhotosFSHandler

class PhotosViewModel(
    photosFSRepository: PhotosFSRepository
) : ViewModel() {

    private val photosFSHandler: PhotosFSHandler = photosFSRepository

    fun storePhoto(path: String, uri: Uri?): String {
        return photosFSHandler.storeImageValues(path, uri)
    }

    fun storeImgFromByte(paramBitMap: Bitmap) : String{
        return photosFSHandler.storeImageFromByte(paramBitMap)
    }

    @Suppress("UNCHECKED_CAST")
    class PhotosViewModelFactory(
        private val photosFSRepository: PhotosFSRepository
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PhotosViewModel::class.java)) {

                return PhotosViewModel(photosFSRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
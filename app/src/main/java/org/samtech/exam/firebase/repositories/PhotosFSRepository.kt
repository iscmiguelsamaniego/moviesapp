package org.samtech.exam.firebase.repositories

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.samtech.exam.interfaces.PhotosFSHandler
import org.samtech.exam.network.pokos.UserPoko
import org.samtech.exam.utils.Utils
import java.io.ByteArrayOutputStream
import java.util.UUID

class PhotosFSRepository : PhotosFSHandler {
    val storage = Firebase.storage
    override fun getImageValues(listener: PhotosFSHandler.CustomPhotosListener): String {
        TODO("Not yet implemented")
    }

    override fun updateImageValues(userId: String, user: UserPoko): String {
        TODO("Not yet implemented")
    }

    override fun storeImageValues(path: String, uri: Uri?): String {
        var storeResult = ""
        val storageRef = storage.reference.child(path)

        storageRef.putFile(uri!!)
            .addOnFailureListener { e ->
                storeResult = "Error en la carga de imágen $e.message"
            }
            .addOnSuccessListener {
                storeResult = "La imagen se ha subido de manera exitosa"
            }

        return storeResult
    }

    override fun storeImageFromByte(paramBitMap: Bitmap): String {
        var storeByteResult = ""
        val storage = Firebase.storage
        val storageRef = storage.reference
            .child(UUID.randomUUID().toString())
        val baos = ByteArrayOutputStream()
        paramBitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener { exception ->
            storeByteResult = "Error en la carga de foto"
        }
        uploadTask.addOnSuccessListener { taskSnapshot ->
            storeByteResult =
                "La foto se ha subido de manera exitosa $taskSnapshot.bytesTransferred"
        }

        return storeByteResult
    }


}
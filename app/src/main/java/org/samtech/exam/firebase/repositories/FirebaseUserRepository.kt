package org.samtech.exam.firebase.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import org.samtech.exam.firebase.models.FSAvatar
import org.samtech.exam.firebase.models.FSUser
import org.samtech.exam.interfaces.UserRepository
import org.samtech.exam.network.pokos.UserPoko
import org.samtech.exam.utils.Constants.COLLECTION_USER
import org.samtech.exam.utils.Constants.KEY_AVATAR

class FirebaseUserRepository : UserRepository {

    var db = FirebaseFirestore.getInstance()

    override fun getUserValues(listener: UserRepository.UserListener): String {
        var getResult = ""

        db.collection(COLLECTION_USER)
            .get()
            .addOnSuccessListener { result ->
                if(!result.isEmpty) {
                    val gson = Gson()
                    for (document in result) {
                        val user = gson.toJson(document.data).toString()
                        val userAux = gson.fromJson(user, FSUser::class.java)
                        val avatarObj = gson.toJson(document.data.getValue(KEY_AVATAR)).toString()
                        val avatar = gson.fromJson(avatarObj, FSAvatar::class.java)

                        listener.onUserResult(
                            FSUser(
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
                    }
                }else{
                    listener.onUserResult(FSUser())
                }
            }
            .addOnFailureListener { e ->
                getResult = "Error : $e.cause"
            }

        return getResult
    }

    override fun updateUserValues(userId: String, user: UserPoko): String {
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

    override fun storeUserValues(user: UserPoko): String {
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
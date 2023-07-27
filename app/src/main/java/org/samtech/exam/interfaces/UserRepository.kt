package org.samtech.exam.interfaces

import org.samtech.exam.firebase.models.FSUser
import org.samtech.exam.network.pokos.UserPoko

interface UserRepository {

    fun getUserValues(listener: UserListener) : String
    fun updateUserValues(userId : String, user: UserPoko) : String
    fun storeUserValues(user: UserPoko): String

    interface UserListener {
        fun onUserResult(user: FSUser)
    }
}
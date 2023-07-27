package org.samtech.exam.firebase.models


import org.samtech.exam.network.pokos.AvatarPoko
import org.samtech.exam.network.pokos.GravatarPoko
import org.samtech.exam.network.pokos.TmdbPoko
import org.samtech.exam.network.pokos.UserPoko

fun FSUserList() : List<UserPoko>{

    return listOf(
        UserPoko(
            AvatarPoko(
                GravatarPoko("thisishash"),
                TmdbPoko("thisispath")
            ),
            1234,
            "iso6391",
            "iso31661",
            "myname",
            false,
            "myusername"
        )
    )
}
package org.samtech.exam.firebase.models

import java.io.Serializable

class FSAvatar(
    var gravatar: FSGravatar = FSGravatar(),
    var tmdb: FSTmdb = FSTmdb()
) : Serializable{
}
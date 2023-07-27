package org.samtech.exam.firebase.models

data class FSUser(
    var fsDocumentId: String? = null,
    var avatar: FSAvatar? = FSAvatar(),
    var id: Int? = null,
    var iso6391: String? = null,
    var iso31661: String? = null,
    var name: String? = null,
    var includeAdult: Boolean? = null,
    var username: String? = null
)
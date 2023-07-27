package org.samtech.exam.network.pokos

import com.google.gson.annotations.SerializedName

data class UserPoko(
    @SerializedName("avatar") var avatar: AvatarPoko? = AvatarPoko(),
    @SerializedName("id") var id: Int? = null,
    @SerializedName("iso_639_1") var iso6391: String? = null,
    @SerializedName("iso_3166_1") var iso31661: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("include_adult") var includeAdult: Boolean? = null,
    @SerializedName("username") var username: String? = null
)
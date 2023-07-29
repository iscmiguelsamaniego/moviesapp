package org.samtech.exam.network.pokos

import com.google.gson.annotations.SerializedName

class AuthorDetailsPoko(
    @SerializedName("name") var name: String? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("avatar_path") var avatarPath: String? = null,
    @SerializedName("rating") var rating: Int? = null
)
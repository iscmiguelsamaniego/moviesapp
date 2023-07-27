package org.samtech.exam.network.pokos

import com.google.gson.annotations.SerializedName

data class AvatarPoko(
    @SerializedName("gravatar") var gravatar: GravatarPoko? = GravatarPoko(),
    @SerializedName("tmdb") var tmdb: TmdbPoko? = TmdbPoko()
)
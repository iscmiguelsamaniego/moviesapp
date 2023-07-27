package org.samtech.exam.network.pokos

import com.google.gson.annotations.SerializedName

data class TmdbPoko(
    @SerializedName("avatar_path" ) var avatarPath : String? = null
)
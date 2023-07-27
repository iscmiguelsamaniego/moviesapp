package org.samtech.exam.network.pokos

import com.google.gson.annotations.SerializedName

data class GravatarPoko(
    @SerializedName("hash" ) var hash : String? = null
)
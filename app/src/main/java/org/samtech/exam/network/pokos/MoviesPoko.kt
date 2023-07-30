package org.samtech.exam.network.pokos

import com.google.gson.annotations.SerializedName

data class MoviesPoko(
    @SerializedName("page") var page: Int? = null,
    @SerializedName("results") var results: ArrayList<ResultsPoko> = arrayListOf(),
    @SerializedName("total_pages") var totalPages: Int? = null,
    @SerializedName("total_results") var totalResults: Int? = null
)
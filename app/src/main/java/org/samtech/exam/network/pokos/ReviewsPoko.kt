package org.samtech.exam.network.pokos

import com.google.gson.annotations.SerializedName

class ReviewsPoko(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("page")
    var page: Int? = null,
    @SerializedName("results")
    var results: ArrayList<ResultsReviewPoko> = arrayListOf(),
    @SerializedName("total_pages")
    var totalPages: Int? = null,
    @SerializedName("total_results")
    var totalResults: Int? = null
)
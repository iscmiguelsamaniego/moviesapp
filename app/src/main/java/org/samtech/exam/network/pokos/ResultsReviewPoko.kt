package org.samtech.exam.network.pokos

import com.google.gson.annotations.SerializedName

class ResultsReviewPoko(
    @SerializedName("author") var author: String? = null,
    @SerializedName("author_details") var authorDetails: AuthorDetailsPoko? = AuthorDetailsPoko(),
    @SerializedName("content") var content: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("url") var url: String? = null
)
package org.samtech.exam.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
class Movies(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int?,
    @ColumnInfo("type") var type: String?,
    @ColumnInfo("adult") var adult: Boolean?,
    @ColumnInfo("backdropPath") var backdropPath: String?,
    @ColumnInfo("genreIds") var genreIds: String,
    @ColumnInfo("originalLanguage") var originalLanguage: String?,
    @ColumnInfo("originalTitle") var originalTitle: String?,
    @ColumnInfo("overview") var overview: String?,
    @ColumnInfo("popularity") var popularity: Double?,
    @ColumnInfo("posterPath") var posterPath: String?,
    @ColumnInfo("releaseDate") var releaseDate: String?,
    @ColumnInfo("title") var title: String?,
    @ColumnInfo("video") var video: Boolean?,
    @ColumnInfo("voteAverage") var voteAverage: Double?,
    @ColumnInfo("voteCount") var voteCount: Int?
)
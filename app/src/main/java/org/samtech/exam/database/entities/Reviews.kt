package org.samtech.exam.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
class Reviews(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int?,
    @ColumnInfo("name") var name : String? = null,
    @ColumnInfo("username") var username : String? = null,
    @ColumnInfo("avatar_path") var avatar_path : String? = null,
    @ColumnInfo("created_at") var created_at : String? = null,
    @ColumnInfo("rating") var rating : String? = null,
    @ColumnInfo("content") var content : String? = null,
)
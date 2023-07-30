package org.samtech.exam.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
class Reviews(
    @ColumnInfo("movieid") var movieid : String?,
    @ColumnInfo("name") var name : String?,
    @ColumnInfo("username") var username : String?,
    @ColumnInfo("avatar_path") var avatar_path : String?,
    @ColumnInfo("created_at") var created_at : String?,
    @ColumnInfo("rating") var rating : String?,
    @ColumnInfo("content") var content : String?
){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
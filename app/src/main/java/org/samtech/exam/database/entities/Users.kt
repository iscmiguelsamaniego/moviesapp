package org.samtech.exam.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class Users(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int?,
    @ColumnInfo("avatarPath") var avatarPath : String? = null,
    @ColumnInfo("iso6391") var iso6391: String? = null,
    @ColumnInfo("iso31661") var iso31661: String? = null,
    @ColumnInfo("name") var name: String? = null,
    @ColumnInfo("includeAdult") var includeAdult: Boolean? = null,
    @ColumnInfo("username") var username: String? = null
)
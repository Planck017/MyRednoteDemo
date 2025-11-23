package com.example.mydemo.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    val userId: Int = 0,

    @ColumnInfo(name = "user_name")
    val userName: String,

    @ColumnInfo(name = "user_password")
    val userPassword: String,

    @ColumnInfo(name = "avatar")
    val avatar: String = ""
)

package com.example.mydemo.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class Comment(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "comment_id")
    val commentId: Int = 0,

    @ColumnInfo(name = "note_id")
    val noteId: Int,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "comment_content")
    val commentContent: String,
)

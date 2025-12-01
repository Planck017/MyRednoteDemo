package com.example.mydemo.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "comment_id")
    val commentId: Int = 0,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "note_id")
    val noteId: Int,

    @ColumnInfo(name = "comment_content")
    val commentContent: String,

    @ColumnInfo(name = "likes")
    val likes: Int = 0,

)

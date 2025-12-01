package com.example.mydemo.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    val noteId: Int = 0,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "note_title")
    val noteTitle: String,

    @ColumnInfo(name = "note_content")
    val noteContent: String,

    @ColumnInfo(name = "likes")
    val likes: Int = 0,

    @ColumnInfo(name = "comments")
    val comments: Int = 0,

) : Serializable
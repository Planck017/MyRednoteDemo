package com.example.mydemo.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class Image(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "image_id")
    val imageId: Int = 0,

    @ColumnInfo(name = "note_id")
    val noteId: Int,

    @ColumnInfo(name = "file_name")
    val fileName: String,
)

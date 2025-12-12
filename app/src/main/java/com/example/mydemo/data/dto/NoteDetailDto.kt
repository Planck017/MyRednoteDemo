package com.example.mydemo.data.dto

data class NoteDetailDto(
    val noteId: Int,

    val userId: Int,
    val userName: String,
    val avatar: Int,

    val noteTitle: String,
    val noteContent: String,

    val imageIds: List<Int>,
    val commentRecycleViewDtos: List<CommentRecycleViewItem>
)

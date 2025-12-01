package com.example.mydemo.repository

import android.util.Log
import com.example.mydemo.data.api.CommentService
import com.example.mydemo.data.model.Comment

class CommentRepository(private val commentService: CommentService) {


    suspend fun getComments(noteId: Int): List<Comment> {
        return try {
            val response = commentService.getComments(noteId)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }

        } catch (e: Exception) {
            Log.e("CommentRepository", "getComments: ", e)
            emptyList()
        }
    }
}
package com.example.mydemo.data.api

import com.example.mydemo.data.model.Comment
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CommentService {
    @GET("getNoteComments/{noteId}")
    suspend fun getComments(
        @Path("noteId") noteId: Int
    ): Response<List<Comment>>
}
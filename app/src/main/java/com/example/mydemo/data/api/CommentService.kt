package com.example.mydemo.data.api

import com.example.mydemo.data.model.Comment
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentService {
    @GET("comment/getNoteComments/{noteId}")
    suspend fun getNoteComments(
        @Path("noteId") noteId: Int
    ): Response<List<Comment>>

    @POST("addNoteComment")
    suspend fun addComment(
        @Body comment: Comment
    ): Response<Long>
}
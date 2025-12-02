package com.example.mydemo.data.api

import com.example.mydemo.data.model.Image
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ImageService {
    @GET("images/getImagesByNoteId/{noteId}")
    suspend fun getImagesByNoteId(
        @Path("noteId") noteId: Int
    ): Response<List<Image>>
}
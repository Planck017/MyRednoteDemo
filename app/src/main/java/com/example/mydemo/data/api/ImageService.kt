package com.example.mydemo.data.api

import com.example.mydemo.data.model.Image
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ImageService {
//    @GET("api/files/note/{noteId}")
//    suspend fun getImagesByNoteId(
//        @Path("noteId") noteId: Int
//    ): Response<List<Image>>

    @GET("images/getImagesByNoteId/{noteId}")
    suspend fun getImagesListByNoteId(
        @Path("noteId") noteId: Int
    ): Response<List<Image>>

    @GET("images/getImageByPath/{imagePath}")
    suspend fun getImageByPath(
        @Path("imagePath") imagePath: String
    ): ByteArray
}
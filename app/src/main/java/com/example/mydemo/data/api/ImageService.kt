package com.example.mydemo.data.api

import com.example.mydemo.data.model.Image
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @Multipart
    @POST("api/files/upload/{noteId}")
    suspend fun uploadImage(
        @Path("noteId") noteId: Long,
        @Part file: MultipartBody.Part
    ): Response<Map<String, String>>
}
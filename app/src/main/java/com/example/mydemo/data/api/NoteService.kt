package com.example.mydemo.data.api

import com.example.mydemo.data.model.Note
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface NoteService {
    @GET("note/getNotes")
    suspend fun getNotes(
        @Query("page") page: Int
    ): Response<List<Note>>
    @GET("note/getNextNotes/{noteId}")
    suspend fun getNextNotes(
        @Path("noteId") noteId: Int
    ): Response<List<Note>>

    @GET("note/getNoteById/{noteId}")
    suspend fun getNoteById(
        @Path("noteId") noteId: Int
    ): Note
//
//    @POST("insertNote")
//    suspend fun insertNote(note: Note)
//
//    @PUT("updateNote")
//    suspend fun updateNote(note: Note)
//
//    @DELETE("deleteNote/{id}")
//    suspend fun deleteNote(id: Int)
}
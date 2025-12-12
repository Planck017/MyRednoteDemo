package com.example.mydemo.repository

import android.util.Log
import android.widget.Toast
import com.example.mydemo.data.api.NoteService
import com.example.mydemo.data.dto.NoteFragmentItem
import com.example.mydemo.data.model.Note
import com.example.mydemo.ui.fragment.HomeFragment
import com.example.mydemo.view.NoteViewModel
import okhttp3.OkHttpClient

class NoteRepository(private val noteService: NoteService) {


    suspend fun getNotes(page: Int) : List<Note> {
        return try {
            // 调用API获取笔记列表
            val response = noteService.getNotes(page)
            // 打印请求URL
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            // 打印异常信息
            Log.e("NoteRepository", "Error fetching notes: ${e.message}")
            emptyList()
        }
    }

    suspend fun getNextNotes(noteId: Int) : List<Note> {
        return try {
            val response = noteService.getNextNotes(noteId)
            Log.d("NoteRepository", "response body size: ${response.body()?.size}")

            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            // 打印异常信息
            Log.e("NoteRepository", "Error fetching next notes: ${e.message}")
            emptyList()
        }
    }

    suspend fun insertNote(note: Note): Int {
        return try {
            val response = noteService.insertNote(note)
            if (response.isSuccessful){
                Log.d("NoteRepository", "insert note success")
            } else {
                Log.e("NoteRepository", "insert note failed: ${response.message()}")
            }
        }catch (e: Exception) {
            // 打印异常信息
            Log.e("NoteRepository", "Error insert note: ${e.message}")
        }
    }

    suspend fun getNoteRecycleViews(noteId: Int): List<NoteFragmentItem> {
        return try {
            val response = noteService.getNoteRecycleViews(noteId)
            if (response.isSuccessful){
                Log.d("NoteRepository", "get note recycle views success")
                response.body() ?: emptyList()
            } else {
                Log.e("NoteRepository", "get note recycle views failed: ${response.message()}")
                emptyList()
            }
        }catch (e: Exception) {
            // 打印异常信息
            Log.e("NoteRepository", "Error get note recycle views: ${e.message}")
            emptyList()
        }
    }
}
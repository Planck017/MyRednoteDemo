package com.example.mydemo.data.dao

import androidx.compose.ui.Modifier
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mydemo.data.model.Note


@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(note: Note)

    @Insert
    suspend fun insertNotes(notes: MutableList<Note>)

    // 获取前20条note
    @Query("SELECT * FROM notes LIMIT 20")
    suspend fun getNext20Notes(): MutableList<Note>

    // 获取id大于等于指定值的note
    @Query("SELECT * FROM notes WHERE note_id >= :id ORDER BY note_id ASC LIMIT 20")
    suspend fun getNotesAfterId(id: Int): MutableList<Note>

    // 获取最新的note_id
    @Query("SELECT MAX(note_id) FROM notes")
    suspend fun getLatestNoteId(): Int?

}
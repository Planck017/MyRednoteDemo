package com.example.mydemo.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mydemo.repository.NoteRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mydemo.data.model.Note

class NoteViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes


     suspend fun loadNotes() : List<Note>{
         return noteRepository.getNotes(0)
     }

     suspend fun loadNextNotes(noteId: Int) : List<Note> {
         return noteRepository.getNextNotes(noteId)
     }

    // 获取当前 LiveData 中的最后一项笔记的 ID
    fun getLastNoteId(): Int? {
        return _notes.value?.lastOrNull()?.noteId
    }

    // 清空 LiveData 中的数据
    fun clearNotes() {
        _notes.value = emptyList()
    }

    // 向 LiveData 中添加新的笔记
    fun addNote(newNotes: List<Note>) {
        // 确保旧数据不为空
        _notes.value = _notes.value.orEmpty() + newNotes
    }

}

class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
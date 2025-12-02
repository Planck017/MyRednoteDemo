package com.example.mydemo.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mydemo.R
import com.example.mydemo.data.AppDatabase
import com.example.mydemo.data.api.NoteService
import com.example.mydemo.data.model.Note
import com.example.mydemo.repository.NoteRepository
import com.example.mydemo.ui.activity.ui.theme.MyDemoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CreateNoteActivity : AppCompatActivity() {


    private lateinit var database: AppDatabase
    private lateinit var editNoteTitle: EditText
    private lateinit var editNoteContent: EditText
    private lateinit var buttonCancel: Button
    private lateinit var buttonSave: Button
    private lateinit var noteService: NoteService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)
        initViews()
        initRetrofit()
        setClickListeners()

    }

    // 添加初始化Retrofit的方法
    private fun initRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://172.21.96.1:8080/") // 替换为你的API基础URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        noteService = retrofit.create(NoteService::class.java)
    }

    private fun initViews() {
        editNoteTitle = findViewById(R.id.editNoteTitle)
        editNoteContent = findViewById(R.id.editNoteContent)
        buttonCancel = findViewById(R.id.buttonCancel)
        buttonSave = findViewById(R.id.buttonSave)
    }

    private fun setClickListeners() {
        buttonCancel.setOnClickListener {
            finish()
        }
        buttonSave.setOnClickListener {
            saveNote()
        }
    }

    private fun saveNote() {
        val title = editNoteTitle.text.toString().trim()
        val content = editNoteContent.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show()
            return
        }
        if (content.isEmpty()) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show()
            return
        }

        val newNote = Note(
            userId = 1,
            noteTitle = title,
            noteContent = content,
            likes = 0,
            comments = 0
        )

        // 调用接口保存笔记
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = noteService.insertNote(newNote)
                Log.d("CreateNoteActivity", "insert note response: $response")

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful){
                        Log.d("CreateNoteActivity", "insert note success")
                        Toast.makeText(this@CreateNoteActivity, "笔记保存成功", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Log.e("CreateNoteActivity", "insert note failed: ${response.message()}")
                        Toast.makeText(this@CreateNoteActivity, "笔记保存失败", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // 打印异常信息
                Log.e("CreateNoteActivity", "Error insert note: ${e.message}")
                kotlinx.coroutines.runBlocking(Dispatchers.Main) {
                    Toast.makeText(this@CreateNoteActivity, "笔记保存失败", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

}


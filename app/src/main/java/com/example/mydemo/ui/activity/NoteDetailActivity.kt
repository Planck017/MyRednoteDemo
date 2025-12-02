package com.example.mydemo.ui.activity

import android.graphics.Bitmap
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mydemo.R
import com.example.mydemo.data.api.CommentService
import com.example.mydemo.data.api.NoteService
import com.example.mydemo.data.api.UserService
import com.example.mydemo.data.model.Comment
import com.example.mydemo.data.model.Note
import com.example.mydemo.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NoteDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NOTE_ID = "extra_note_id"
        const val EXTRA_NOTE_TITLE = "extra_note_title"
    }

    private lateinit var noteService: NoteService
    private lateinit var userService: UserService
    private lateinit var commentService: CommentService

    override fun onCreate(savedInstanceState: Bundle?) {
        // 优化1: 延迟加载ContentView
        super.onCreate(null) // 避免自动恢复状态，提高启动速度
        // 优化2: 预设主题以减少白屏时间
        setTheme(R.style.Theme_MyDemo)
        setContentView(R.layout.activity_note_detail)


        // 获取传递过来的Note ID
        val noteId = intent.getIntExtra(EXTRA_NOTE_ID, -1)
        val noteTitle = intent.getStringExtra(EXTRA_NOTE_TITLE)
        
        if (noteId == -1) {
            Log.e("NoteDetailActivity", "未接收到有效的Note ID")
            finish()
            return
        }
        
        Log.d("NoteDetailActivity", "接收到Note ID: $noteId")


        // 初始化视图
        initViews(noteId, noteTitle ?: "笔记 $noteId")

        // 初始化Retrofit
        initRetrofit()

        fetchNoteDetails(noteId)
        fetchNoteUserInfo(noteId)
        fetchNoteUserAvatar(noteId)
    }

    private fun initRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://172.21.96.1:8080/") // 替换为你的API基础URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        noteService = retrofit.create(NoteService::class.java)
        userService = retrofit.create(UserService::class.java)
        commentService = retrofit.create(CommentService::class.java)
    }

    private fun initViews(noteId: Int, noteTitle: String) {

        val detailTitleText = findViewById<TextView>(R.id.detailTitleText)
        val detailContentText = findViewById<TextView>(R.id.detailContentText)
        val detailLikeCountText = findViewById<TextView>(R.id.detailLikeCountText)
        val detailCommentCountText = findViewById<TextView>(R.id.detailCommentCountText)
        val backButton = findViewById<ImageButton>(R.id.backButton)

        // 设置标题
        detailTitleText.text = noteTitle
        
        // 优化3: 异步加载内容
        // 设置返回按钮点击事件
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun fetchNoteDetails(noteId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("NoteDetailActivity", "开始获取笔记详情，ID: $noteId")
                // 调用API获取笔记详情
                val note = noteService.getNoteById(noteId)
                Log.d("NoteDetailActivity", "成功获取笔记详情: ${note.noteTitle}")

                // 在主线程更新UI
                withContext(Dispatchers.Main) {
                    displayNoteDetails(note)
                }
            } catch (e: Exception) {
                Log.e("NoteDetailActivity", "获取笔记详情失败", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@NoteDetailActivity, "获取笔记详情失败: ${e.message}", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }


    // 获取发布note的用户信息
    private fun fetchNoteUserInfo(userId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("NoteDetailActivity", "开始获取用户信息，ID: $userId")
                // 调用API获取用户信息
                val user = userService.getUserById(userId)
                Log.d("NoteDetailActivity", "成功获取用户信息: ${user.userName}")
                // 在主线程更新UI
                withContext(Dispatchers.Main) {
                    displayNoteUserInfo(user.userName)
                }
            } catch (e: Exception) {
                Log.e("NoteDetailActivity", "获取用户信息失败", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@NoteDetailActivity, "获取用户信息失败: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // 显示note发布用户名称
    private fun displayNoteUserInfo(name: String) {
        findViewById<TextView>(R.id.userNameText).text = name
    }

    // 获取用户头像
    private fun fetchNoteUserAvatar(userId: Int) {
        // 使用Glide直接加载图像
        val imageView = findViewById<ImageView>(R.id.userAvatar)
        val imageUrl = "http://172.21.96.1:8080/users/getUserAvatar/$userId"

        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_user) // 设置占位图
            .error(R.drawable.ic_add) // 设置错误图片
            .into(imageView)

        Log.d("NoteDetailActivity", "开始加载用户头像: $imageUrl")
    }


    // 获取评论
    private suspend fun fetchComments(noteId: Int): List<Comment>? {
        return try {
            val response: Response<List<Comment>> = commentService.getNoteComments(noteId)
            if (response.isSuccessful && response.body() is List<Comment>) {
                Log.d("NoteDetailActivity", "成功获取评论，数量: ${response.body()?.size}")
                response.body()
            } else {
                Log.e("NoteDetailActivity", "获取评论失败，响应码: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("NoteDetailActivity", "获取评论失败", e)
            null
        }
    }

    private fun displayNoteDetails(note: Note) {
        findViewById<TextView>(R.id.detailTitleText).text = note.noteTitle
        findViewById<TextView>(R.id.detailContentText).text = note.noteContent
        findViewById<TextView>(R.id.detailLikeCountText).text = "Likes: ${note.likes}"
        findViewById<TextView>(R.id.detailCommentCountText).text = "Comments: ${note.comments}"
    }
}
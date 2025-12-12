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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mydemo.BuildConfig
import com.example.mydemo.R
import com.example.mydemo.data.api.CommentService
import com.example.mydemo.data.api.ImageService
import com.example.mydemo.data.api.NoteService
import com.example.mydemo.data.api.UserService
import com.example.mydemo.data.dto.NoteDetailDto
import com.example.mydemo.data.dto.CommentRecycleViewItem
import com.example.mydemo.ui.adpter.CommentAdapter
import com.example.mydemo.ui.adpter.ImageAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private lateinit var imageService: ImageService
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var commentAdapter: CommentAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
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

        // 获取笔记详情
        fetchNoteDetails(noteId)
    }

    private fun initRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL) // 替换为你的API基础URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        noteService = retrofit.create(NoteService::class.java)
        userService = retrofit.create(UserService::class.java)
        commentService = retrofit.create(CommentService::class.java)
        imageService = retrofit.create(ImageService::class.java)
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

        // 初始化图片RecyclerView
        val imageRecyclerView = findViewById<ViewPager2>(R.id.imageViewPager)
        imageRecyclerView.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        imageAdapter = ImageAdapter(emptyList())
        imageRecyclerView.adapter = imageAdapter

        // 初始化评论RecyclerView
        val commentRecyclerView = findViewById<RecyclerView>(R.id.commentRecyclerView)
        commentRecyclerView.layoutManager = LinearLayoutManager(this)
        commentAdapter = CommentAdapter(emptyList())
        commentRecyclerView.adapter = commentAdapter
    }

    private fun fetchNoteDetails(noteId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("NoteDetailActivity", "开始获取笔记详情，ID: $noteId")
                // 调用API获取笔记详情
                val note = noteService.getNoteDetail(noteId).body() ?: NoteDetailDto(
                    noteId = -1,
                    userId = -1,
                    userName = "未知用户",
                    avatar = -1,
                    noteTitle = "笔记 $noteId",
                    noteContent = "笔记内容",
                    imageIds = emptyList(),
                    commentRecycleViewDtos = emptyList()
                )
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


    // 获取笔记图片
    // TODO 获取笔记图片
    private fun fetchNoteImages(noteId: List<Int>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                    // 在主线程更新UI
                    withContext(Dispatchers.Main) {
                        // 更新现有adapter的数据而不是创建新的adapter
                        (imageAdapter as ImageAdapter).apply {
                            // 这里需要修改ImageAdapter以支持更新数据
                            notifyDataSetChanged() // 需要在ImageAdapter中添加更新数据的方法
                        }
                        // 或者直接重新设置adapter的数据
                        val imageViewPager = findViewById<ViewPager2>(R.id.imageViewPager)
                        imageViewPager.adapter = ImageAdapter(noteId)
                    }

            } catch (e: Exception) {
                Log.e("NoteDetailActivity", "获取笔记图片失败", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@NoteDetailActivity, "获取笔记图片失败: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // 获取用户头像
    private fun fetchNoteUserAvatar(avatar: Int) {
        // 使用Glide直接加载图像
        val imageView = findViewById<ImageView>(R.id.userAvatar)
        val imageUrl =  BuildConfig.API_BASE_URL + "images/getImage/$avatar"

        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_user) // 设置占位图
            .error(R.drawable.ic_launcher_background) // 设置错误图片
            .into(imageView)

        Log.d("NoteDetailActivity", "开始加载用户头像: $imageUrl")
    }


    // 获取评论
    private fun fetchComments(comments: List<CommentRecycleViewItem>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                    // 在主线程更新UI
                    withContext(Dispatchers.Main) {
                        commentAdapter = CommentAdapter(comments)
                        val commentRecyclerView = findViewById<RecyclerView>(R.id.commentRecyclerView)
                        commentRecyclerView.adapter = commentAdapter
                    }

            } catch (e: Exception) {
                Log.e("NoteDetailActivity", "获取笔记评论失败", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@NoteDetailActivity, "获取笔记评论失败: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun displayNoteDetails(note: NoteDetailDto) {
        findViewById<TextView>(R.id.detailTitleText).text = note.noteTitle
        findViewById<TextView>(R.id.detailContentText).text = note.noteContent
        findViewById<TextView>(R.id.userNameText).text = note.userName

        // 头像
        fetchNoteUserAvatar(note.avatar)

        // 图片
        fetchNoteImages(note.imageIds)

        // 评论
        fetchComments(note.commentRecycleViewDtos)
    }


}
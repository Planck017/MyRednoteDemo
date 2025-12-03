package com.example.mydemo.ui.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydemo.R
import com.example.mydemo.data.api.ImageService
import com.example.mydemo.data.api.NoteService
import com.example.mydemo.data.model.Note
import com.example.mydemo.ui.adpter.SelectedImageAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody

import java.io.File

class CreateNoteActivity : AppCompatActivity(), SelectedImageAdapter.OnImageRemoveListener {

    private lateinit var editNoteTitle: EditText
    private lateinit var editNoteContent: EditText
    private lateinit var buttonSelectImages: Button
    private lateinit var buttonCancel: Button
    private lateinit var buttonSave: Button
    private lateinit var recyclerViewSelectedImages: RecyclerView
    private lateinit var selectedImageAdapter: SelectedImageAdapter
    private lateinit var noteService: NoteService
    private lateinit var imageService: ImageService

    private val selectedImageUris = mutableListOf<Uri>()

    // 注册ActivityResultLauncher以选择图片
    private val selectImagesLauncher = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        uris.forEach { uri ->
            selectedImageUris.add(uri)
        }
        selectedImageAdapter.notifyDataSetChanged()
    }

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
        imageService = retrofit.create(ImageService::class.java)
    }

    private fun initViews() {
        editNoteTitle = findViewById(R.id.editNoteTitle)
        editNoteContent = findViewById(R.id.editNoteContent)
        buttonSelectImages = findViewById(R.id.buttonSelectImages)
        buttonCancel = findViewById(R.id.buttonCancel)
        buttonSave = findViewById(R.id.buttonSave)
        recyclerViewSelectedImages = findViewById(R.id.recyclerViewSelectedImages)

        // 初始化图片预览RecyclerView
        selectedImageAdapter = SelectedImageAdapter(selectedImageUris, this)
        recyclerViewSelectedImages.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewSelectedImages.adapter = selectedImageAdapter
    }

    private fun setClickListeners() {
        buttonSelectImages.setOnClickListener {
            selectImages()
        }

        buttonCancel.setOnClickListener {
            finish()
        }
        buttonSave.setOnClickListener {
            saveNote()
        }
    }

    private fun selectImages() {
        // 启动图片选择器
        selectImagesLauncher.launch("image/*")
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
                        val noteId = response.body() ?: 0
                        uploadImages(noteId)
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


    private fun getFileFromUri(uri: Uri): File {
        val contentResolver = contentResolver
        val inputStream = contentResolver.openInputStream(uri)

        // 获取原始文件名和扩展名
        val originalFileName = getFileName(uri)
        val fileName = if (originalFileName != null) {
            originalFileName
        } else {
            // 如果无法获取原始文件名，则根据MIME类型生成带扩展名的文件名
            val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
            val extension = when {
                mimeType.startsWith("image/") -> mimeType.substringAfter("/")
                else -> "jpg"
            }
            "temp_image_${System.currentTimeMillis()}.$extension"
        }

        val file = File(cacheDir, fileName)

        Log.d("CreateNoteActivity", "正在处理文件: $fileName, URI: $uri")

        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        Log.d("CreateNoteActivity", "文件创建成功，路径: ${file.absolutePath}, 大小: ${file.length()} 字节")

        return file
    }

    private fun getFileName(uri: Uri): String? {
        var fileName: String? = null
        when (uri.scheme) {
            "content" -> {
                contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex("_display_name")
                    if (nameIndex != -1 && cursor.moveToFirst()) {
                        fileName = cursor.getString(nameIndex)
                    }
                }
            }
            "file" -> {
                fileName = uri.lastPathSegment
            }
        }
        Log.d("CreateNoteActivity", "从URI获取文件名: $fileName")
        return fileName
    }



    private fun uploadImages(noteId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                selectedImageUris.forEach { uri ->
                    // 将Uri转换为RequestBody（这里需要具体实现）
                    val file = getFileFromUri(uri)
                    Log.d("CreateNoteActivity", "file path: ${file.absolutePath}")
                    val mimeType = contentResolver.getType(uri) ?: "image/*"
                    Log.d("CreateNoteActivity", "mimeType: $mimeType")
                    // 创建RequestBody，保留原始文件类型
                    val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())


                    // 创建MultipartBody.Part，使用原始文件名
                    val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                    // 调用上传接口
                    val response = imageService.uploadImage(noteId, body)

                    if (response.isSuccessful) {
                        Log.d("CreateNoteActivity", "图片上传成功: ${response.body()}")
                    } else {
                        Log.e("CreateNoteActivity", "图片上传失败: ${response.errorBody()}")
                    }
                }

            } catch (e: Exception) {
                Log.e("CreateNoteActivity", "Error uploading images: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CreateNoteActivity, "图片上传失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onImageRemove(position: Int) {
        selectedImageAdapter.removeImage(position)
    }
}


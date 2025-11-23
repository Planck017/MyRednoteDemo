package com.example.mydemo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mydemo.data.AppDatabase
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {


    private lateinit var database: AppDatabase
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        database = AppDatabase.getInstance(this)

        usernameEditText = findViewById<EditText>(R.id.username)
        passwordEditText = findViewById<EditText>(R.id.password)
        loginButton = findViewById<Button>(R.id.login)
        
        insertSampleUserIfNotExists()

        loginButton.setOnClickListener {
            handleLogin()
        }


    }

    // 处理登录逻辑
    private fun handleLogin() {

        val username = findViewById<EditText>(R.id.username).text.toString().trim()
        val password = findViewById<EditText>(R.id.password).text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val user = database.userDao().getUserByUsernameAndPassword(username, password)
            if (user != null) {

                // 登录成功，保存token到sp中
                saveTokenToSharedPreferences(user.username)
                // 登录成功，显示一个Toast提示
                Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()
                // 登录成功，跳转到主界面
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // 登录失败，显示一个Toast提示
                Toast.makeText(this@LoginActivity, "用户名或密码错误", Toast.LENGTH_SHORT).show()
            }
        }

    }

    // 存下token到sp中
    private fun saveTokenToSharedPreferences(token: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    // 插入一个测试用户到数据库中（如果不存在）
    private fun insertSampleUserIfNotExists() {
        lifecycleScope.launch {
            if (!database.userDao().isUsernameExists("testuser")) {
                val sampleUser = com.example.mydemo.data.model.User(
                    username = "testuser",
                    password = "password123"
                )
                database.userDao().insertUser(sampleUser)
            }
        }
    }

}
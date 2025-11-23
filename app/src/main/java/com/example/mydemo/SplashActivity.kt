package com.example.mydemo

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({

            // 尝试从sharedPreferences中获取token
            val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val token: String? = sharedPreferences.getString("token", null)
            if (token != null) {
                // token存在，进行后续操作
                // 例如，跳转到主界面
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                // 例如，跳转到主界面
            } else {
                // token不存在，进行登录操作
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                // 例如，跳转到登录界面
            }
            finish()
        }, 1000)
    }
}

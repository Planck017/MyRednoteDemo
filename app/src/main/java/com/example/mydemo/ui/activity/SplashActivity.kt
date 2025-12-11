package com.example.mydemo.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.mydemo.R
import com.example.mydemo.util.SharePreferencesUtil


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({

            // 尝试从sharedPreferences中获取token
            SharePreferencesUtil.init(this)
            val instance = SharePreferencesUtil.getInstance()
            val token: String? = instance.get("token")
            if(!instance.contains("token")){
                // token不存在，进行登录操作
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else{
                // token存在，进行后续操作
                // 暂时直接登录 到主界面
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                // TODO 后续操作 1. 检查token有效性 2. 刷新token 3. 解析token中的用户信息 4. 存储用户信息到sharedPreferences
            }

            finish()
        }, 1000)
    }
}

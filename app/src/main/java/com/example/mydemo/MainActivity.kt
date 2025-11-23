package com.example.mydemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 设置底部导航栏
        setBottomNavigationBar()
    }

    private fun setBottomNavigationBar() {
        bottomNavigationView = findViewById(R.id.bottom_navigation)

    }
}


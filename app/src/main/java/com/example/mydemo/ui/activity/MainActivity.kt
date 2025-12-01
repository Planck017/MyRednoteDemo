package com.example.mydemo.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mydemo.R
import com.example.mydemo.ui.fragment.AddFragment
import com.example.mydemo.ui.fragment.HomeFragment
import com.example.mydemo.ui.fragment.MarketFragment
import com.example.mydemo.ui.fragment.MessageFragment
import com.example.mydemo.ui.fragment.MyselfFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 设置底部导航栏
        setBottomNavigationBar()
        // 默认显示首页 Fragment
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment, HomeFragment())
                .commit()
        }
    }

    private fun setBottomNavigationBar() {
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        // 设置底部导航栏选择监听器
        bottomNavigationView.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null

            when (item.itemId) {
                R.id.navigation_home -> selectedFragment = HomeFragment()
                R.id.navigation_market -> selectedFragment = MarketFragment()
                R.id.navigation_add -> selectedFragment = AddFragment()
                R.id.navigation_message -> selectedFragment = MessageFragment()
                R.id.navigation_myself -> selectedFragment = MyselfFragment()
            }

            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, selectedFragment)
                    .commit()
                true
            } else {
                false
            }
        }

        // 设置默认选中项
        bottomNavigationView.selectedItemId = R.id.navigation_home
    }

}


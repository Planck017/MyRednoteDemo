package com.example.mydemo

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mydemo.data.AppDatabase
import com.example.mydemo.data.model.Note
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var database: AppDatabase

     @Before
    fun setUp() {
        database = AppDatabase.getInstance(InstrumentationRegistry.getInstrumentation().context)
    }

    @After
    fun tearDown() {
        database.close()
    }


    // 测试初始化note表

    // 测试添加笔记


}
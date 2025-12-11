package com.example.mydemo.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SharePreferencesUtil {
    companion object {
        private const val PREFS_NAME = "MyPrefs"
        private const val KEY_TOKEN = "token"
        private var instance: SharePreferencesUtil? = null
        private lateinit var sharedPreferences: SharedPreferences

        fun init(context: Context) {
            sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        }

        /**
         * 获取 SharedPreferencesUtil 单例实例
         * @return SharedPreferencesUtil 实例
         */
        fun getInstance(): SharePreferencesUtil {
            if (instance == null) {
                synchronized(SharePreferencesUtil::class) {
                    if (instance == null) {
                        instance = SharePreferencesUtil()
                    }
                }
            }
            return instance!!
        }
    }

    /**
     * 检查是否包含指定的key
     * @param key 要检查的key
     * @return 如果包含指定的key则返回true，否则返回false
     */
    fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    /**
     * 保存任意类型的值到SharedPreferences
     * @param key 要保存的key
     * @param value 要保存的值
     */
    fun put(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    /**
     * 从SharedPreferences中获取指定key的值
     * @param key 要获取的key
     * @return 如果存在指定的key则返回对应的值，否则返回null
     */
    fun get(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    /**
     * 从SharedPreferences中移除指定key的值
     * @param key 要移除的key
     */
    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    /**
     * 清空SharedPreferences中的所有数据
     */
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

}
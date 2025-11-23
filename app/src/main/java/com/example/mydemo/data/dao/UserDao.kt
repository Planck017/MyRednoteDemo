package com.example.mydemo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mydemo.data.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE user_name = :userName")
    suspend fun getUserByUsername(userName: String): User?

    @Query("SELECT * FROM users WHERE user_name = :userName AND user_password = :userPassword")
    suspend fun getUserByUsernameAndPassword(userName: String, userPassword: String): User?

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE user_name = :userName)")
    suspend fun isUsernameExists(userName: String): Boolean

    @Insert
    suspend fun insertUser(sampleUser: User)
}
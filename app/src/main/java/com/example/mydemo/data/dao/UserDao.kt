package com.example.mydemo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mydemo.data.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE user_name = :username")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM users WHERE user_name = :username AND user_password = :password")
    suspend fun getUserByUsernameAndPassword(username: String, password: String): User?

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE user_name = :username)")
    suspend fun isUsernameExists(username: String): Boolean

    @Insert
    suspend fun insertUser(sampleUser: User)
}
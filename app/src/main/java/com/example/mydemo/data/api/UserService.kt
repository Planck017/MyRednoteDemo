package com.example.mydemo.data.api

import android.graphics.Bitmap
import android.media.Image
import com.example.mydemo.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {

    @GET("users/{userId}")
    suspend fun getUserById(
        @Path("userId") userId: Int
    ): User

    // 获取用户头像
    @GET("users/getUserAvatar/{userId}")
    suspend fun getUserAvatar(
        @Path("userId") userId: Int
    ): ByteArray

}
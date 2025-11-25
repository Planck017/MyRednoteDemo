package com.example.mydemo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mydemo.data.dao.NoteDao
import com.example.mydemo.data.dao.UserDao
import com.example.mydemo.data.model.Comment
import com.example.mydemo.data.model.Image
import com.example.mydemo.data.model.Note
import com.example.mydemo.data.model.User

@Database(
    entities = [User::class, Comment::class, Note::class, Image::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun noteDao(): NoteDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "planck_database"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                instance
            }
        }


    }

}
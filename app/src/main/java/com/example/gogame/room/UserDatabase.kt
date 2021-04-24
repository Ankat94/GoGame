package com.example.shaadi.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gogame.model.User
import com.example.shaadi.utils.Converters


const val DATABASE_VERSION = 2


//add entity here
@Database(
        entities = [User::class],
        version = DATABASE_VERSION)
@TypeConverters(Converters::class)
abstract class UserDatabase : RoomDatabase() {

    companion object {

        private const val DATABASE_NAME = "UserDB"

        fun buildDatabase(context: Context): UserDatabase {
            return Room.databaseBuilder(
                    context,
                    UserDatabase::class.java,
                    DATABASE_NAME
            )
                    .allowMainThreadQueries().fallbackToDestructiveMigration()
                    .build()
        }
    }

    abstract fun userDao(): UserDao

}
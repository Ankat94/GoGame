package com.example.shaadi.room

import androidx.room.*
import com.example.gogame.model.User


@Dao
interface UserDao {

    @Query("select * from User")
    fun getAllUsers(): List<User>

    @Query("select * from User where id = (:id)")
    fun getUser(id: Long): User

    @Query("select * from User where favourite = 1")
    fun getFavorites(): List<User>

    @Insert
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)
}
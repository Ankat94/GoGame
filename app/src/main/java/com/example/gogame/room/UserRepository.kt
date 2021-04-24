package com.example.shaadi.room

import android.content.Context

import androidx.lifecycle.MutableLiveData
import com.example.gogame.model.User


class UserRepository(val context: Context) {

    var newMutable : MutableLiveData<List<User>> = MutableLiveData()
    var userDao = UserDatabase.buildDatabase(context).userDao()

    fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }

    fun getFavorites(): List<User> {
        return userDao.getFavorites()
    }

    fun getUser(id: Long): User {
        return userDao.getUser(id)
    }

    fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    fun insertUser(user: User) {
        userDao.insertUser(user)
    }

}
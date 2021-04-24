package com.example.gogame.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.assignment1.util.Coroutines
import com.example.shaadi.room.UserRepository

class MainViewModel(val context: Context): ViewModel() {

    var newMutable : MutableLiveData<List<User>> = MutableLiveData()
    var newLiveData: LiveData<List<User>> = newMutable
    var userRepository = UserRepository(context)

    fun getFavoriteUsers(): LiveData<List<User>> {
        Coroutines.IO {
            newMutable.postValue(userRepository.getFavorites())
        }
        return newMutable
    }
}
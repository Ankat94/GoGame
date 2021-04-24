package com.example.gogame.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.assignment1.util.Coroutines
import com.example.shaadi.room.UserRepository
import kotlinx.coroutines.CoroutineScope

class ContactViewModel(context: Context) : ViewModel() {

    var newMutable : MutableLiveData<User> = MutableLiveData()
    var newLiveData: LiveData<User> = newMutable
    var userRepository = UserRepository(context)

    fun getUser(id: Long) {
        Coroutines.IO {
            newMutable.postValue(userRepository.getUser(id))
        }
    }

    fun updateUser(user: User) {
        Coroutines.Main {
            userRepository.updateUser(user)
        }
    }

    fun insertUser(user: User) {
        Coroutines.Main {
            userRepository.insertUser(user)
        }
    }

    fun deleteUser(user: User) {
        Coroutines.Main {
            userRepository.deleteUser(user)
        }
    }

}
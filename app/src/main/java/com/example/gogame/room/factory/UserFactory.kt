package com.kira.store.mvvm.crm.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gogame.model.ContactViewModel
import com.example.gogame.model.MainViewModel


class UserFactory(val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(context) as T
        }
        if(modelClass.isAssignableFrom(ContactViewModel::class.java)) {
            return ContactViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
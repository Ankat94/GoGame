package com.kira.store.mvvm.crm.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class UserFactory(val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
//            return MainViewModel(context) as T
//        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
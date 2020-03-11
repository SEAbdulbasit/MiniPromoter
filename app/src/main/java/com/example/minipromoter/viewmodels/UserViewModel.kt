package com.example.minipromoter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.App

//
// Created by Abdul Basit on 2/19/2020.
// Copyright (c) 2020 VisionX. All rights reserved.
//

class UserViewModel(private val prodcutName: String) : BaseViewModel() {

    val userList = App.getUserRepository().getProdcutUsersList(produtName = prodcutName)


    class Factory(val prodcutName: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(prodcutName) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}

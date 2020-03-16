package com.example.minipromoter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.App

//
// Created by Abdul Basit on 2/19/2020.
// Copyright (c) 2020 VisionX. All rights reserved.
//

class FragmentProductViewModel :
    BaseViewModel() {

    val prodcutList = App.getUserRepository().getProductsList()


    class Factory :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FragmentProductViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FragmentProductViewModel() as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}

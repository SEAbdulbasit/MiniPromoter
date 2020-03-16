package com.example.minipromoter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.App
import com.example.minipromoter.models.ToolbarModel

//
// Created by Abdul Basit on 3/12/2020.
//

class CampainViewModel(private val prodcutName: String) : BaseViewModel() {


    val campainList = App.getUserRepository().getProductCampains(prodcutName)


    class Factory(private val prodcutName: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CampainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CampainViewModel(prodcutName) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }

}
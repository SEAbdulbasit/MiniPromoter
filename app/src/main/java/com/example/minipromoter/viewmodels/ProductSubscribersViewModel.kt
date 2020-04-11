package com.example.minipromoter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProductSubscribersViewModel : ViewModel() {


    class Factory :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductSubscribersViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProductSubscribersViewModel() as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}

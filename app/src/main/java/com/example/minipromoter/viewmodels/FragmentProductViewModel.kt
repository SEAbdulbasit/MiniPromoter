package com.example.minipromoter.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.App


class FragmentProductViewModel :
    BaseViewModel() {

    //products live data to get data from db
    val product = App.getUserRepository().database.productDao.getAllProducts()

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

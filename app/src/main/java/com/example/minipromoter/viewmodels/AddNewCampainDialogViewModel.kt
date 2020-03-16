package com.example.minipromoter.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.App
import com.example.minipromoter.models.Campain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

//
// Created by Abdul Basit on 3/12/2020.
//

class AddNewCampainDialogViewModel(private val productName: String) : BaseViewModel() {

    private var viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    val tittle = MutableLiveData("")
    val message = MutableLiveData("")
    val onButtonClicked = MutableLiveData(false)


    fun onAddClicked() {
        onButtonClicked.value = true
        onButtonClicked.value = false
    }

    fun addNewCampain() {
        coroutineScope.launch(Dispatchers.IO) {
            val campain =
                Campain(
                    campainName = tittle.value!!,
                    campainMessage = message.value!!,
                    productId = productName
                )
            App.getUserRepository().insertCampain(campain)
        }
    }


        class Factory(private val prodcutName: String) :
            ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AddNewCampainDialogViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return AddNewCampainDialogViewModel(prodcutName) as T
                }
                throw IllegalArgumentException("Unable to construct view model")
            }
        }


}
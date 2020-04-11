package com.example.minipromoter.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.App
import com.example.minipromoter.models.Campaign
import com.example.minipromoter.models.ProductModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

//
// Created by Abdul Basit on 3/12/2020.
//

class AddNewCampaignDialogViewModel(private val productModel: ProductModel) : BaseViewModel() {

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
                Campaign(
                    campaignName = tittle.value!!,
                    campaignMessage = message.value!!,
                    productId = productModel.productId
                )
            App.getUserRepository().insertCampaign(campain)
        }
    }


    class Factory(private val productModel: ProductModel) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddNewCampaignDialogViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AddNewCampaignDialogViewModel(productModel) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }


}
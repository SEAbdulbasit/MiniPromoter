package com.example.minipromoter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.App
import com.example.minipromoter.models.ProductModel

//
// Created by Abdul Basit on 3/12/2020.
//

class CampaignViewModel(productModel: ProductModel) : BaseViewModel() {


    val campaignList = App.getUserRepository().getProductCampaigns(productModel.productId)


    class Factory(private val prodcutName: ProductModel) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CampaignViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CampaignViewModel(prodcutName) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }

}
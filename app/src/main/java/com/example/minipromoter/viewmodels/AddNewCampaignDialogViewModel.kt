package com.example.minipromoter.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.App
import com.example.minipromoter.Utils.Event
import com.example.minipromoter.models.Campaign
import com.example.minipromoter.models.Keywords
import com.example.minipromoter.models.ProductModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

//
// Created by Abdul Basit on 3/12/2020.
//

class AddNewCampaignDialogViewModel(val productModel: ProductModel) : BaseViewModel() {

    private var viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    val tittle = MutableLiveData("")
    val message = MutableLiveData("")
    val expireMessage = MutableLiveData("")
    val onButtonClicked = MutableLiveData<Event<Unit>>()


    fun onAddClicked() {
        onButtonClicked.value = Event(Unit)
    }

    fun addNewCampaign() {
        coroutineScope.launch(Dispatchers.IO) {
            val campaign =
                Campaign(
                    productId = productModel.productId,
                    campaignMessage = message.value,
                    expiryAutoMessageReply = expireMessage.value


                )
            val campaignId = App.getUserRepository().database.campaignDao.insertCampaign(campaign)
            insertPrimaryKeywords(campaignId)


        }
    }

    private fun insertPrimaryKeywords(campaignId: Long) {

        val subKeyword =
            Keywords(description = "Sub ${productModel.productName}", campaignId = campaignId)
        val unSubKeyword =
            Keywords(description = "unSub ${productModel.productName}", campaignId = campaignId)

        App.getUserRepository().database.keywordsDao.insertKeywords(subKeyword)
        App.getUserRepository().database.keywordsDao.insertKeywords(unSubKeyword)


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
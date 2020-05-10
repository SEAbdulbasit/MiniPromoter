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

    //coroutine scope so we can cancel the job if view model is destroyed
    private var viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    // mutable live data which are bind to view through 2 way data binding
    val tittle = MutableLiveData("")
    val message = MutableLiveData("")
    val expireMessage = MutableLiveData("")

    // event type data so we can send events to fragment
    val onButtonClicked = MutableLiveData<Event<Unit>>()


    fun onAddClicked() {
        onButtonClicked.value = Event(Unit)
    }

    //function to add new campaign
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

    //function to insert the default keywords in keywords table
    private fun insertPrimaryKeywords(campaignId: Long) {

        val subKeyword =
            Keywords(description = "sub ${productModel.productName}", campaignId = campaignId)
        val unSubKeyword =
            Keywords(description = "unsub ${productModel.productName}", campaignId = campaignId)

        // adding those models into db
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
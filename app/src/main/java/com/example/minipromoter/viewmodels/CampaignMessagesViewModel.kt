package com.example.minipromoter.viewmodels

import android.telephony.SmsManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.App
import com.example.minipromoter.models.Campaign
import com.example.minipromoter.models.CampaignMessages
import kotlinx.coroutines.*

class CampaignMessagesViewModel(val model: Campaign) : ViewModel() {

    private var viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val campaignMessages = App.getUserRepository().getAllCampaignMessages(model.campaignId)
    val productSubscribers = App.getUserRepository().getProductSubscribers(model.productId)


    fun startSendingMessage() {

        productSubscribers.value?.forEach {
            sendMessage(it.phoneNumber, model.campaignMessage)

        }

        insertMessageIntoCampaignMessages()

    }

    private fun insertMessageIntoCampaignMessages() {

        coroutineScope.launch(Dispatchers.IO) {
            val campaignMessage =
                CampaignMessages(
                    campaignMessage = model.campaignMessage,
                    campaignId = model.campaignId
                )
            App.getUserRepository().insertCampaignMessage(campaignMessage)
        }

    }

    private fun sendMessage(number: String, message: String) {
        val smsManager: SmsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(number, null, message, null, null)

    }


    class Factory(val model: Campaign) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CampaignMessagesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CampaignMessagesViewModel(model) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}

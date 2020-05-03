package com.example.minipromoter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.App
import com.example.minipromoter.models.Campaign
import com.example.minipromoter.models.CampaignMessages
import com.example.minipromoter.models.UserMessage
import kotlinx.coroutines.*


class CampaignMessagesViewModel(val model: Campaign) : ViewModel() {

    private var viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val keywords = App.getUserRepository().database.keywordsDao.getAllKeywords()
    val campaignMessage =
        App.getUserRepository().database.campaignMessageDao.getAllCampaignMessages()
    val productSubscribers =
        App.getUserRepository().database.productSubscribersDao.getProductSubscribers(model.productId)

    val hashmapWithPhoneAndSMSID = HashMap<String, Long>()


    fun startSendingMessage() {
        insertMessageIntoCampaignMessages()
    }

    fun messageSuccessfullySend(phoneNumber: String) {
        GlobalScope.launch {
            val messageId = hashmapWithPhoneAndSMSID[phoneNumber]
            val userMessage =
                App.getUserRepository().database.userMessageDao.getUserLastMessage(messageId!!)
            userMessage.isSuccessfullySend = true
            App.getUserRepository().database.userMessageDao.updateUserMessage(userMessage)

        }
    }

    private fun insertMessageIntoCampaignMessages() {

        //insert into campaign message
        coroutineScope.launch(Dispatchers.IO) {
            val campaignMessage =
                CampaignMessages(
                    message = model.campaignMessage,
                    campaignId = model.campaignId
                )
            App.getUserRepository().database.campaignMessageDao.insertCampaignMessage(
                campaignMessage
            )
        }

        //insert into user message
        coroutineScope.launch {
            productSubscribers.value?.forEach {
                val messageId = App.getUserRepository().database.userMessageDao.insertUserMessage(
                    UserMessage(
                        message = model.campaignMessage,
                        userId = it.userId
                    )
                )
                hashmapWithPhoneAndSMSID[it.phoneNumber!!] = messageId
            }
        }
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

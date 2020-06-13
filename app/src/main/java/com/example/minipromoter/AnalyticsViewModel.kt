package com.example.minipromoter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.minipromoter.models.*
import com.example.minipromoter.viewmodels.BaseViewModel
import kotlinx.coroutines.*
import timber.log.Timber

class AnalyticsViewModel : BaseViewModel() {

    //coroutine scope
    private var viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    lateinit var getAllCampaign: List<Campaign>
    lateinit var getAllUserMessages: List<UserMessage>
    lateinit var getAllKeywords: List<Keywords>
    lateinit var getAllCampaignMessages: List<CampaignMessages>
    lateinit var getAllProducts: List<ProductModel>

    val dataList = MutableLiveData<List<ValueDataEntry>>()

    init {
        getDBDATA()
    }

    fun getDBDATA() {
        coroutineScope.launch(Dispatchers.IO) {
            getAllCampaign =
                App.getUserRepository().database.campaignDao.getAllCampaignsWithOutLiveData()
            getAllUserMessages =
                App.getUserRepository().database.userMessageDao.getAllMessagesWithOutLiveData()
            getAllKeywords =
                App.getUserRepository().database.keywordsDao.getAllKeywordsWithOutLiveData()
            getAllCampaignMessages =
                App.getUserRepository().database.campaignMessageDao.getAllCampaignMessagesWithOutLiveData()
            getAllProducts =
                App.getUserRepository().database.productDao.getAllProductsWithOutLiveData()

            getMostActiveCampaign()
        }

    }

    fun getMostActiveCampaign() {

        coroutineScope.launch(Dispatchers.IO) {

            delay(2000)
            mostSubscribedProductToday(getAllUserMessages)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getProductsCampaign() {
        coroutineScope.launch(Dispatchers.IO) {
            productCampaings(getAllCampaign)
        }
    }
/*
    fun mostActiveCampaign(campaignMessage: List<CampaignMessages>?) {
        val results = campaignMessage?.filter {
            it.createdOn < System.currentTimeMillis() && it.createdOn > (System.currentTimeMillis() - 86400000)
        }?.groupBy { it.campaignId }

        // Timber.d("Most Active Campaigns Results Are : %s", results)
    }*/

    fun mostSubscribedProductToday(userMessages: List<UserMessage>?) {
        val results = userMessages?.filter {
            it.createdOn < System.currentTimeMillis() && it.createdOn > (System.currentTimeMillis() - 86400000) && !it.isConversationMessage
        }

        val keywordsList = mutableListOf<Keywords>()

        results?.forEach { userMEssage ->
            val keywords =
                getAllKeywords.find {
                    it.inviteMessage.equals(userMEssage.message, ignoreCase = true)
                }

            keywords?.let { keywordsList.add(it) }
        }

        val campaing = keywordsList.groupBy { it.campaignId }

        val campaingKeys = keywordsList.groupBy { it.campaignId }.keys

        val selectedCamapings =
            getAllCampaign.filter { campaingKeys.contains(it.campaignId) }
                ?.groupBy { it.productId }


        val list = mutableListOf<ValueDataEntry>()
        selectedCamapings.forEach { mapkey ->

            list.add(
                ValueDataEntry(
                    getAllProducts.find { it.productId == mapkey.key }?.productName,
                    calculateMessages(
                        mapkey.value,
                        campaing
                    )
                )
            )

        }

        dataList.postValue(list)
    }

    private fun calculateMessages(
        value: List<Campaign>,
        campaing: Map<Long, List<Keywords>>
    ): Int {

        var count = 0

        value.forEach {
            count += campaing[it.campaignId]?.size ?: 0
        }

        return count
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun productCampaings(campaingsList: List<Campaign>?) {
        val results = campaingsList?.groupBy { it.productId }
        Timber.d("Product Cmapign Results Are : %s", results)
    }

    class Factory :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AnalyticsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AnalyticsViewModel() as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }

}
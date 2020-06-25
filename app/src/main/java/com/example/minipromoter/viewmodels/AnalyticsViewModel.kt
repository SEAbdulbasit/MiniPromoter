package com.example.minipromoter.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.minipromoter.App
import com.example.minipromoter.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

class AnalyticsViewModel : BaseViewModel() {

    //coroutine scope
    private var viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private lateinit var getAllCampaign: List<Campaign>
    private lateinit var getAllUserMessages: List<UserMessage>
    private lateinit var getAllKeywords: List<Keywords>
    private lateinit var getAllCampaignMessages: List<CampaignMessages>
    private lateinit var getAllProducts: List<ProductModel>

    val todayMostActiveProduct = MutableLiveData<List<ValueDataEntry>>()
    val todayMostActiveCampaign = MutableLiveData<List<ValueDataEntry>>()

    init {
        getDBDATA()
    }

    private fun getDBDATA() {
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

    private fun getMostActiveCampaign() {

        coroutineScope.launch(Dispatchers.IO) {
            mostSubscribedProductToday(getAllUserMessages)
        }
    }

    private fun groupByWeek(userMessages: List<UserMessage>?) {
        val results = userMessages?.filter {
            it.createdOn < System.currentTimeMillis() && it.createdOn > (System.currentTimeMillis() - (86400000 * 7)) && !it.isConversationMessage
        }

        val hasMap = HashMap<Long, List<UserMessage>>()
        val cal = Calendar.getInstance()

        results?.forEach {
            cal.timeInMillis = it.createdOn
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            if (hasMap.containsKey(cal.timeInMillis)) {
                val userMessagesList = hasMap[cal.timeInMillis]
                userMessages.toMutableList().add(it)
                hasMap[cal.timeInMillis] = userMessagesList?.toList()!!
            } else {
                hasMap[cal.timeInMillis] = mutableListOf(it)
            }
        }
    }

    private fun mostSubscribedProductToday(userMessages: List<UserMessage>?) {
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

        val campaignHashMap = keywordsList.groupBy { it.campaignId }

        val campaignKeys = keywordsList.groupBy { it.campaignId }.keys

        val selectedCampaigns =
            getAllCampaign.filter { campaignKeys.contains(it.campaignId) }
                .groupBy { it.productId }


        val todayActiveProductData = mutableListOf<ValueDataEntry>()
        val todayActiveCampaignData = mutableListOf<ValueDataEntry>()
        selectedCampaigns.forEach { manky ->
            todayActiveProductData.add(
                ValueDataEntry(
                    getAllProducts.find { it.productId == manky.key }?.productName,
                    calculateMessages(
                        manky.value,
                        campaignHashMap
                    )
                )
            )
        }


        campaignHashMap.forEach { mahmap ->

            todayActiveCampaignData.add(
                ValueDataEntry(
                    getAllCampaign.find { it.campaignId == mahmap.key }?.campaignTittle,
                    mahmap.value.size

                )
            )
        }

        coroutineScope.launch(Dispatchers.Main) {
            todayMostActiveProduct.postValue(todayActiveProductData)
            todayMostActiveCampaign.postValue(todayActiveCampaignData)
        }
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
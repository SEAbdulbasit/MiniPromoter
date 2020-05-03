package com.example.minipromoter.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import com.example.minipromoter.App
import com.example.minipromoter.fragment.ProductSubscribers
import com.example.minipromoter.models.SubscribersProductsCrossRef
import com.example.minipromoter.models.UserMessage
import com.example.minipromoter.models.UserModel
import com.example.minipromoter.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

//
// Created by Abdul Basit on 3/8/2020.
//

class IncomingSMS : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras
        Log.d("IncomingSMS", "Hi, i am here")
        var str = ""
        if (bundle != null) {
            var from: String = ""
            var smsBody: String = ""
            val pdus = bundle.get("pdus") as Array<*>
            for (onePdus: Any? in pdus) {
                val oneSMS = SmsMessage.createFromPdu(onePdus as ByteArray)
                str += "SMS from " + oneSMS.originatingAddress
                str += " :"
                str += oneSMS.messageBody.toString()
                str += "\n"
                from = oneSMS.originatingAddress!!
                smsBody = oneSMS.messageBody.toString()
            }
            runBlocking(Dispatchers.IO) {
                processTheMessage(from, smsBody)
            }
        }
    }


    suspend fun processTheMessage(sender: String, message: String) {
        try {
            val userRepository = UserRepository()
            val keyword = userRepository.database.keywordsDao.getKeywordByMessage(message)

            if (keyword != null) {

                val userModel = userRepository.database.userDao.findUserByPhoneNumber(sender)
                val campaignModel =
                    userRepository.database.campaignDao.getCampaignById(keyword.campaignId)
                val productModel =
                    userRepository.database.productDao.getProductById(campaignModel.productId)

                var userid: Long?
                if (userModel == null) {
                    userid =
                        userRepository.database.userDao.insertUser(UserModel(phoneNumber = sender))
                } else {
                    userid = userModel.userId
                }

                val productSubscribers =
                    userRepository.database.productSubscribersDao.getProductAndUserSubscriber(
                        userid,
                        productModel.productId
                    )
                if (productSubscribers == null) {
                    val productSubscribers2 =
                        SubscribersProductsCrossRef(
                            userId = userid,
                            productId = productModel.productId
                        )
                    userRepository.database.productSubscribersDao.insert(productSubscribers2)
                }

                val userMessage =
                    UserMessage(userId = userid, message = message, isIncomingMessage = true)
                userRepository.database.userMessageDao.insertUserMessage(userMessage)


            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
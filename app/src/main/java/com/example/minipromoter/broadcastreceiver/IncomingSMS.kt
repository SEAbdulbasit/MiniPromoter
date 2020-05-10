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

        // getting the incoming message details
        if (bundle != null) {
            var from = ""
            var smsBody = ""
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

                // processing the incoming message
                processTheMessage(from, smsBody)
            }
        }
    }


    private fun processTheMessage(sender: String, message: String) {
        try {
            val userRepository = UserRepository()

            // getting the keywords against that message
            val keyword = userRepository.database.keywordsDao.getKeywordByMessage(message)

            // checking if we have keyword like this
            if (keyword != null) {

                // getting the user model
                val userModel = userRepository.database.userDao.findUserByPhoneNumber(sender)

                // getting the campaign model
                val campaignModel =
                    userRepository.database.campaignDao.getCampaignById(keyword.campaignId)

                //getting the product model
                val productModel =
                    userRepository.database.productDao.getProductById(campaignModel.productId)

                val userid: Long?
                userid = userModel?.userId ?: userRepository.database.userDao.insertUser(UserModel(phoneNumber = sender))

                var productSubscribers =
                    userRepository.database.productSubscribersDao.getProductAndUserSubscriber(
                        userid,
                        productModel.productId
                    )
                if (productSubscribers == null) {
                    productSubscribers =
                        SubscribersProductsCrossRef(
                            userId = userid,
                            productId = productModel.productId
                        )
                    userRepository.database.productSubscribersDao.insert(productSubscribers)
                } else {

                    if (keyword.description?.contains("unsub", ignoreCase = true)!!) {
                        productSubscribers.isActive = false

                    } else if (keyword.description?.contains("sub", ignoreCase = true)!!) {
                        productSubscribers.isActive = true

                    }
                    userRepository.database.productSubscribersDao.update(productSubscribers)

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
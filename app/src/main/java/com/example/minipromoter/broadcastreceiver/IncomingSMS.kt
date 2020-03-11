package com.example.minipromoter.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
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
            val pdus = bundle.get("pdus") as Array<Any>
            for (onePdus: Any in pdus) {
                val oneSMS = SmsMessage.createFromPdu(onePdus as ByteArray)
                str += "SMS from " + oneSMS.originatingAddress
                str += " :"
                str += oneSMS.messageBody.toString()
                str += "\n"
                from = oneSMS.originatingAddress!!
                smsBody = oneSMS.messageBody.toString()

            }



            runBlocking(Dispatchers.IO) {
                val userRepository = UserRepository()
                val productList = userRepository.getProductsListWithOutLiveData()
                val messageParts = smsBody.split("#")
                if (messageParts[1] != null) {
                    val product = productList.findLast {
                        it.productName.equals(messageParts[0])
                    }
                    if (product != null) {
                        val userModel = UserModel(phoneNumber = from, productId = messageParts[1])
                        userRepository.insertUser(userModel)

                    }
                }

            }
            //}
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
        }
    }
}
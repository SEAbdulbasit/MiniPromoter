package com.example.minipromoter.fragment

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.minipromoter.adapter.CampaignMessageOnClickListener
import com.example.minipromoter.adapter.CampaignMessagesAdapter
import com.example.minipromoter.adapter.KeywordsAdapter
import com.example.minipromoter.adapter.KeywordsClickListner
import com.example.minipromoter.databinding.FragmentCampainMessagesBinding
import com.example.minipromoter.viewmodels.CampaignMessagesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class CampaignMessagesFragment : Fragment() {

    private val args: CampaignMessagesFragmentArgs by navArgs()

    private val viewModel: CampaignMessagesViewModel by lazy {
        ViewModelProvider(
            this,
            CampaignMessagesViewModel.Factory(args.model)
        ).get(CampaignMessagesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentCampainMessagesBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.rvKeywords.adapter = KeywordsAdapter(KeywordsClickListner {

        })

        binding.rvKeywords.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        viewModel.keywords.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                val adapter = binding.rvKeywords.adapter as KeywordsAdapter
                adapter.submitList(it)
            }
        })

        binding.rvCampaignMessages.adapter =
            CampaignMessagesAdapter(CampaignMessageOnClickListener {
            })

        viewModel.campaignMessage.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                val adapter = binding.rvCampaignMessages.adapter as CampaignMessagesAdapter
                adapter.submitList(it)
            }
        })


        binding.fbAdd.setOnClickListener {
            viewModel.startSendingMessage()
            sendMessage(viewModel.model.campaignMessage!!)
        }

        return binding.root
    }


    private fun sendMessage(message: String) {

        viewModel.productSubscribers.value?.forEach { userModel ->
            GlobalScope.launch(Dispatchers.IO) {

                val SENT = "SMS_SENT"
                val sentPI = PendingIntent.getBroadcast(context!!, 0, Intent(SENT), 0)

                activity!!.registerReceiver(object : BroadcastReceiver() {
                    override fun onReceive(arg0: Context, arg1: Intent) {
                        when (resultCode) {
                            Activity.RESULT_OK -> {
                                Toast.makeText(
                                    context!!,
                                    "SMS sent",
                                    Toast.LENGTH_LONG
                                ).show()
                                viewModel.messageSuccessfullySend(userModel.phoneNumber!!)

                            }
                            SmsManager.RESULT_ERROR_GENERIC_FAILURE -> Toast.makeText(
                                context!!,
                                "Generic failure",
                                Toast.LENGTH_LONG
                            ).show()
                            SmsManager.RESULT_ERROR_NO_SERVICE -> Toast.makeText(
                                context!!,
                                "No service",
                                Toast.LENGTH_LONG
                            ).show()
                            SmsManager.RESULT_ERROR_NULL_PDU -> Toast.makeText(
                                context!!,
                                "Null PDU",
                                Toast.LENGTH_LONG
                            ).show()
                            SmsManager.RESULT_ERROR_RADIO_OFF -> Toast.makeText(
                                context!!,
                                "Radio off",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }, IntentFilter(SENT))


                val smsManager: SmsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(userModel.phoneNumber, null, message, sentPI, null)

            }
        }

    }

}

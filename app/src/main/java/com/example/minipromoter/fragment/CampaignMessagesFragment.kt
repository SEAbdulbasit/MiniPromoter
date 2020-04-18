package com.example.minipromoter.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.minipromoter.adapter.CampaignMessageOnClickListener
import com.example.minipromoter.adapter.CampaignMessagesAdapter
import com.example.minipromoter.databinding.FragmentCampainMessagesBinding
import com.example.minipromoter.viewmodels.CampaignMessagesViewModel

/**
 * A simple [Fragment] subclass.
 */
class CampaignMessagesFragment : Fragment() {
    val args: CampaignMessagesFragmentArgs by navArgs()

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

        binding.rvCampaignMessages.adapter =
            CampaignMessagesAdapter(CampaignMessageOnClickListener {
            })

        viewModel.campaignMessages.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                val adapter = binding.rvCampaignMessages.adapter as CampaignMessagesAdapter
                adapter.submitList(it)
            }
        })

        viewModel.productSubscribers.observe(viewLifecycleOwner, Observer {
            Log.d("CampaingnMessage", it.size.toString())
        })

        binding.fbAdd.setOnClickListener {
            viewModel.startSendingMessage()
        }

        return binding.root
    }

}

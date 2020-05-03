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
import com.example.minipromoter.viewmodels.ProductSubscribersViewModel
import com.example.minipromoter.R
import com.example.minipromoter.adapter.UserAdapter
import com.example.minipromoter.adapter.UserOnClickListener
import com.example.minipromoter.databinding.ProductSubscribersFragmentBinding


class ProductSubscribers : Fragment() {
    private val args: ProductSubscribersArgs by navArgs()

    private val viewModel: ProductSubscribersViewModel by lazy {
        ViewModelProvider(
            this,
            ProductSubscribersViewModel.Factory(args.productModel)
        ).get(ProductSubscribersViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = ProductSubscribersFragmentBinding.inflate(inflater)


        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = UserAdapter(UserOnClickListener {
            findNavController().navigate(
                ProductSubscribersDirections.actionProductSubscribersToChatsFragment(
                    it
                )
            )
        })

        binding.rvUsers.adapter = adapter
        viewModel.userList.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                adapter.submitList(it)
            }
        })

        return binding.root
    }


}

package com.example.minipromoter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.minipromoter.adapter.UserAdapter
import com.example.minipromoter.adapter.UserOnClickListener
import com.example.minipromoter.databinding.ProductSubscribersFragmentBinding
import com.example.minipromoter.models.UserModel
import com.example.minipromoter.viewmodels.ProductSubscribersViewModel


class ProductSubscribers : Fragment() {

    // arguments from previous fragment
    private val args: ProductSubscribersArgs by navArgs()

    // view models for fragment
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

        // assigning the view model and lifecycle
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //adapter object
        val adapter = UserAdapter(object : UserOnClickListener {
            override fun onClick(client: UserModel) {
                findNavController().navigate(
                    ProductSubscribersDirections.actionProductSubscribersToChatsFragment(
                        client
                    )


                )
            }
        }
        )

        //assigning adapter to recyclerview
        binding.rvUsers.adapter = adapter

        //observing the live data of users
        viewModel.userList.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                adapter.submitList(it)
            }
        })

        return binding.root
    }


}

package com.example.minipromoter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.viewmodels.ProductSubscribersViewModel
import com.example.minipromoter.R


class ProductSubscribers : Fragment() {

    private val viewModel: ProductSubscribersViewModel by lazy {
        ViewModelProvider(
            this,
            ProductSubscribersViewModel.Factory()
        ).get(ProductSubscribersViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.product_subscribers_fragment, container, false)
    }


}

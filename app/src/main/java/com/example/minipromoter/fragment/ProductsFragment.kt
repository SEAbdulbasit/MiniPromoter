package com.example.minipromoter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.minipromoter.adapter.OnClickListener
import com.example.minipromoter.adapter.ProductSelectionAdapter
import com.example.minipromoter.databinding.FragmentProductsBinding
import com.example.minipromoter.dialogs.AddNewProductDialog
import com.example.minipromoter.viewmodels.FragmentProductViewModel

class ProductsFragment : Fragment() {

    private val viewModel: FragmentProductViewModel by lazy {
        ViewModelProvider(
            this,
            FragmentProductViewModel.Factory()
        ).get(FragmentProductViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentProductsBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.rvProduct.adapter = ProductSelectionAdapter(OnClickListener(clickListener = {

            val directions =
                ProductsFragmentDirections.actionProductsFragmentToCampainsFragment(it)
            findNavController().navigate(directions)

        }, subscriberListner = {
            findNavController().navigate(
                ProductsFragmentDirections.actionProductsFragmentToProductSubscribers(
                    productModel = it
                )
            )

        }))

        binding.rvProduct.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        viewModel.product.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                val adapter = binding.rvProduct.adapter as ProductSelectionAdapter
                adapter.submitList(it)
            }
        })

        binding.fbAdd.setOnClickListener {
            showAddNewProductFragment()
        }

        return binding.root
    }


    private fun showAddNewProductFragment() {
        AddNewProductDialog.newInstance().show(childFragmentManager, "dialog")

    }

}

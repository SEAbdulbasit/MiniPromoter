package com.example.minipromoter.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.minipromoter.R
import com.example.minipromoter.adapter.OnClickListener
import com.example.minipromoter.adapter.ProductSelectionAdapter
import com.example.minipromoter.databinding.FragmentProductsBinding
import com.example.minipromoter.dialogs.AddNewProductDialog
import com.example.minipromoter.models.ProductModel
import com.example.minipromoter.viewmodels.FragmentProductViewModel
import timber.log.Timber

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

        //assigning adapter to recycle view
        binding.rvProduct.adapter = ProductSelectionAdapter(object : OnClickListener {
            override fun onClick(client: ProductModel) {
                val directions =
                    ProductsFragmentDirections.actionProductsFragmentToCampainsFragment(client)
                findNavController().navigate(directions)
            }

            override fun onSubscribersClicked(client: ProductModel) {
                findNavController().navigate(
                    ProductsFragmentDirections.actionProductsFragmentToProductSubscribers(
                        productModel = client
                    )
                )
            }
        }

        )

        // adding line each item in recycle view
        binding.rvProduct.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        //observing products live data so we can notify the adapter
        viewModel.product.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                val adapter = binding.rvProduct.adapter as ProductSelectionAdapter
                adapter.submitList(it)
            }
        })

        //floating action button click listener
        binding.fbAdd.setOnClickListener {

            // show add new product dialog
            showAddNewProductFragment()
        }

        return binding.root
    }


    private fun showAddNewProductFragment() {

        // creating new instance of dialog and showing
        AddNewProductDialog.newInstance().show(childFragmentManager, "dialog")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.products_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuAnalytics -> {
                Timber.d("Analytics clicked")
                val intent = Intent(requireContext(), Analytics::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}

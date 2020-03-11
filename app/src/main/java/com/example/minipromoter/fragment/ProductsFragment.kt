package com.example.minipromoter.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.minipromoter.App
import com.example.minipromoter.R
import com.example.minipromoter.adapter.OnClickListener
import com.example.minipromoter.adapter.ProductSelectionAdapter
import com.example.minipromoter.databinding.FragmentProductsBinding
import com.example.minipromoter.databinding.ProductDialogBinding
import com.example.minipromoter.viewmodels.FragmentProductViewModel
import kotlinx.android.synthetic.main.alert_label_editor.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

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
        // Inflate the layout for this fragment

        val binding = FragmentProductsBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.rvProduct.layoutManager = GridLayoutManager(activity, 2)

        binding.rvProduct.adapter = ProductSelectionAdapter(OnClickListener {

            val directions =
                ProductsFragmentDirections.actionProductsFragmentToUserFragment(it.productName)
            findNavController().navigate(directions)

        })

        viewModel.prodcutList.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                val adapter = binding.rvProduct.adapter as ProductSelectionAdapter
                adapter.submitList(it)
            }
        })



        binding.ivAddIcon.setOnClickListener {
            showAlertDialog(context!!)
        }

        return binding.root
    }

    fun observerVariables() {

    }


    private fun showAlertDialog(context: Context) {

        val dialogView = layoutInflater.inflate(R.layout.alert_label_editor, null)

        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle("Product Name")

        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE,
            "OK"
        ) { p0, p1 ->
            println("Input Text : ${dialogView.etName}")

            runBlocking {
                App.getUserRepository().insertProduct(dialogView.etName.text.toString())
            }
            p0.dismiss()

        }
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE,
            "Cancel"
        ) { p0, p1 ->

            p0.dismiss()
        }

        alertDialog.setView(dialogView)
        alertDialog.show()

    }

}

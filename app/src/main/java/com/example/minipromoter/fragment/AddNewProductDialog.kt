package com.example.minipromoter.fragment

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.R
import com.example.minipromoter.databinding.FragmentAddNewProductDialogBinding
import com.example.minipromoter.viewmodels.AddNewProductDialogViewModel
import kotlinx.android.synthetic.main.fragment_add_new_campain_dialog_list_dialog.*

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    AddNewCampainDialog.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */


class AddNewProductDialog : BottomSheetDialogFragment() {

    private var productName: String? = null

    private val viewModel: AddNewProductDialogViewModel by lazy {
        ViewModelProvider(
            this,
            AddNewProductDialogViewModel.Factory()
        ).get(AddNewProductDialogViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentAddNewProductDialogBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        observerVariables()

        return binding.root


    }

    private fun observerVariables() {

        viewModel.onButtonClicked.observe(viewLifecycleOwner, Observer {
            if (it) {

                when {
                    viewModel.tittle.value!!.isEmpty() -> ilName.error =
                        getString(R.string.enter_product_name)
                    else -> {
                        viewModel.addNewProduct()
                        dismiss()
                    }
                }

            }
        })
    }


    companion object {

        fun newInstance(): AddNewProductDialog =
            AddNewProductDialog()
    }
}

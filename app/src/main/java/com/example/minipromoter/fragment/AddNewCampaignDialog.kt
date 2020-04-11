package com.example.minipromoter.fragment

import android.os.Bundle
import android.os.Parcelable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.R
import com.example.minipromoter.databinding.FragmentAddNewCampainDialogListDialogBinding
import com.example.minipromoter.models.ProductModel
import com.example.minipromoter.viewmodels.AddNewCampaignDialogViewModel
import kotlinx.android.synthetic.main.fragment_add_new_campain_dialog_list_dialog.*

// TODO: Customize parameter argument names
const val ARG_ITEM_COUNT = "item_count"

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    AddNewCampainDialog.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */

private const val PRODUCT_NAME = "param1"

class AddNewCampaignDialog : BottomSheetDialogFragment() {

    private var productName: ProductModel? = null

    private val viewModel: AddNewCampaignDialogViewModel by lazy {
        ViewModelProvider(
            this,
            AddNewCampaignDialogViewModel.Factory(productName!!)
        ).get(AddNewCampaignDialogViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productName = it.getParcelable(PRODUCT_NAME)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentAddNewCampainDialogListDialogBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        observerVaraibles()

        return binding.root


    }

    private fun observerVaraibles() {

        viewModel.onButtonClicked.observe(viewLifecycleOwner, Observer {
            if (it) {

                when {
                    viewModel.tittle.value!!.isEmpty() -> ilName.error =
                        getString(R.string.enter_campain_name)
                    viewModel.message.value!!.isEmpty() -> tiMessage.error =
                        getString(R.string.enter_campain_message)
                    else -> {
                        viewModel.addNewCampain()
                        dismiss()
                    }
                }

            }
        })
    }


    companion object {

        fun newInstance(product: ProductModel): AddNewCampaignDialog =
            AddNewCampaignDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(PRODUCT_NAME, product as Parcelable)

                }
            }

    }
}

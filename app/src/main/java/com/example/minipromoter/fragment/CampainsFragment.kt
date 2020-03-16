package com.example.minipromoter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.minipromoter.adapter.CampainAdapter
import com.example.minipromoter.adapter.CampainOnClickListener
import com.example.minipromoter.databinding.FragmentCampainsBinding
import com.example.minipromoter.viewmodels.CampainViewModel
import kotlinx.android.synthetic.main.fragment_campains.*

/**
 * A simple [Fragment] subclass.
 */
class CampainsFragment : Fragment() {

    private val args: CampainsFragmentArgs by navArgs()

    private val viewModel: CampainViewModel by lazy {
        ViewModelProvider(
            this,
            CampainViewModel.Factory(args.productName)
        ).get(CampainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = FragmentCampainsBinding.inflate(inflater)

        binding.rvCampains.adapter = CampainAdapter(CampainOnClickListener {

        })

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.fbAdd.setOnClickListener {
            AddNewCampainDialog.newInstance(args.productName).show(childFragmentManager, "dialog")

        }

        observeVariables()

        return binding.root
    }

    fun observeVariables() {
        viewModel.campainList.observe(viewLifecycleOwner, Observer {
            if (!it.isEmpty()) {
                val adapter = rvCampains.adapter as CampainAdapter
                adapter.submitList(it)
            }
        })
    }

}

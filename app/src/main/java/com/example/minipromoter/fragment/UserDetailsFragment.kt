package com.example.minipromoter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.minipromoter.Utils.EventObserver
import com.example.minipromoter.databinding.UserDetailsFragmentBinding
import com.example.minipromoter.viewmodels.UserDetailsViewModel

class UserDetailsFragment : Fragment() {

    private val args: UserDetailsFragmentArgs by navArgs()

    private val viewModel: UserDetailsViewModel by lazy {
        ViewModelProvider(
            this,
            UserDetailsViewModel.Factory(args.userId)
        ).get(UserDetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = UserDetailsFragmentBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        observeVariables()

        return binding.root
    }

    private fun observeVariables() {
        viewModel.user.observe(viewLifecycleOwner, Observer {
            viewModel.userRetrieved(it)
        })

        viewModel.userUpdated.observe(viewLifecycleOwner, EventObserver {
            findNavController().popBackStack()
        })
    }

}
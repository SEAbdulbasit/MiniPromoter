package com.example.minipromoter.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.minipromoter.R
import com.example.minipromoter.adapter.UserMessageOnClickListener
import com.example.minipromoter.adapter.UserMessagesAdapter
import com.example.minipromoter.databinding.FragmentChatsBinding
import com.example.minipromoter.viewmodels.ChatsViewModel
import timber.log.Timber

class ChatsFragment : Fragment() {
    private val args: ChatsFragmentArgs by navArgs()

    private val viewModel: ChatsViewModel by lazy {
        ViewModelProvider(
            this,
            ChatsViewModel.Factory(args.userModel)
        ).get(ChatsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChatsBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //assigning adapter
        binding.rvMessages.adapter = UserMessagesAdapter(UserMessageOnClickListener { })

        //observing the user message so we can notify
        viewModel.userMessages.observe(viewLifecycleOwner, Observer {
            val adapter = binding.rvMessages.adapter as UserMessagesAdapter
            adapter.submitList(it)
        })

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.chats_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuUserDetails -> {
                Timber.d("Analytics clicked")
                findNavController().navigate(
                    ChatsFragmentDirections.actionChatsFragmentToUserDetailsFragment(
                        args.userModel.userId
                    )
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
